package com.budjb.asynchronoustasks

import grails.test.mixin.integration.Integration
import grails.transaction.Rollback
import spock.lang.Specification

@Integration
@Rollback
class SimpleFunctionalitySpec extends Specification {
    def 'Ensure a simple persistent task functions correctly'() {
        setup:
        PersistentAsynchronousTask task = new PersistentAsynchronousTask('simpleTask', 'A basic task for testing.') {
            @Override
            protected void process() {
                save {
                    complete('simple task completed')
                }
            }
        }

        expect:
        task.taskId > 0
        task.state == AsynchronousTaskState.NOT_RUNNING
        task.results == null

        when:
        task.run()

        then:
        task.progress == 100
        task.state == AsynchronousTaskState.COMPLETED
        task.results == 'simple task completed'
    }
}
