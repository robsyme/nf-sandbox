package nextflow.sandbox

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import nextflow.Session
import nextflow.trace.TraceObserver

@Slf4j
@CompileStatic
class SandboxObserver implements TraceObserver {

    @Override
    void onFlowCreate(Session session) {
        log.info "🏖️  Sandbox plugin loaded!"
    }

} 