package nextflow.sandbox

import groovy.transform.CompileStatic
import nextflow.Session
import nextflow.trace.TraceObserver
import nextflow.trace.TraceObserverFactory

@CompileStatic
class SandboxFactory implements TraceObserverFactory {
    @Override
    Collection<TraceObserver> create(Session session) {
        return [new SandboxObserver()]
    }
} 