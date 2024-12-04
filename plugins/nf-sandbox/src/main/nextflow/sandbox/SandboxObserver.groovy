package nextflow.sandbox

import groovy.json.JsonOutput
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import nextflow.Session
import nextflow.processor.TaskHandler
import nextflow.processor.TaskRun
import nextflow.trace.TraceObserver
import nextflow.trace.TraceRecord
import nextflow.script.ScriptMeta
import nextflow.script.bundle.ResourcesBundle

@Slf4j
@CompileStatic
class SandboxObserver implements TraceObserver {

    @Override
    void onFlowCreate(Session session) {
        log.info "🏖️  Sandbox plugin loaded!"
    }
    @Override
    void onProcessSubmit(TaskHandler handler, TraceRecord trace) {
        final task = handler.getTask()
        try {
            // Collect all the hash inputs
            final keys = []
            final session = task.processor.session

            // Add session unique ID
            keys.add(['type': 'session.uniqueId', 'value': session.uniqueId])
            
            // Add process name
            keys.add(['type': 'process.name', 'value': task.processor.name])
            
            // Add task source
            keys.add(['type': 'task.source', 'value': task.source])

            // Add container fingerprint if enabled
            if( task.isContainerEnabled() )
                keys.add(['type': 'container.fingerprint', 'value': task.getContainerFingerprint()])

            // Add input values
            task.inputs.each { param, value ->
                keys.add([
                    'type': 'input',
                    'name': param.name,
                    'value': value.toString()
                ])
            }

            // Add script vars
            Map<String,Object> vars = task.getGlobalVars(task.processor.ownerScript.binding) as Map<String,Object>
            
            // Get task extension directives from config
            Map<String,Object> directives = [:]
            if( task.config?.properties ) {
                task.config.properties.each { key, value ->
                    def keyStr = key.toString()
                    if( keyStr.startsWith('ext.') ) {
                        directives.put("task.${keyStr}".toString(), value)
                    }
                }
            }
            
            if( vars || directives ) {
                vars.putAll(directives)
                keys.add(['type': 'script.vars', 'value': vars.entrySet().toString()])
            }

            // Add bin entries - collect from both module bundle and executor
            List<String> binPaths = []
            // Get module bundle bin dirs
            if( session.enableModuleBinaries() ) {
                def script = task.processor.getOwnerScript()
                def meta = ScriptMeta.get(script)
                if( meta?.isModule() ) {
                    ResourcesBundle bundle = meta.getModuleBundle()
                    if( bundle ) {
                        binPaths.addAll(bundle.getBinDirs()*.toString())
                    }
                }
            }
            // Get executor bin dir
            def executorBin = task.processor.executor.binDir
            if( executorBin )
                binPaths.add(executorBin.toString())

            if( binPaths ) {
                keys.add(['type': 'bin.paths', 'value': binPaths.join('; ')])
            }

            // Add modules
            def modules = task.getConfig().getModule()
            if( modules ) {
                keys.add(['type': 'modules', 'value': modules.toString()])
            }

            // Add conda env
            def conda = task.getCondaEnv()
            if( conda ) {
                keys.add(['type': 'conda', 'value': conda])
            }

            // Add spack env
            def spack = task.getSpackEnv()
            if( spack ) {
                keys.add(['type': 'spack', 'value': spack])
            }

            // Write to task directory
            final inputsFile = task.workDir.resolve('.command.inputs.json')
            inputsFile.text = JsonOutput.prettyPrint(JsonOutput.toJson(keys))
        }
        catch( Exception e ) {
            log.error "Error collecting sandbox keys: ${e.message}", e
        }
    }
} 
