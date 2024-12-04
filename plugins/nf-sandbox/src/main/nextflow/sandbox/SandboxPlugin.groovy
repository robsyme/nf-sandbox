package nextflow.sandbox

import groovy.transform.CompileStatic
import nextflow.plugin.BasePlugin
import org.pf4j.PluginWrapper

@CompileStatic
class SandboxPlugin extends BasePlugin {
    SandboxPlugin(PluginWrapper wrapper) {
        super(wrapper)
    }
} 