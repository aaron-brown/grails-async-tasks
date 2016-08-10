package com.budjb.asynchronoustasks

import grails.plugins.Plugin

class AsyncTasksGrailsPlugin extends Plugin {
    /**
     * Author.
     */
    def author = "Bud Byrd"

    /**
     * Author email.
     */
    def authorEmail = "bud.byrd@gmail.com"

    /**
     * Plugin description.
     */
    def description = 'The asynchronous tasks plugin contains classes that provide a framework for long-running tasks.'

    /**
     * Required Grails version.
     */
    def grailsVersion = "3.1 > *"

    /**
     * URL to the plugin's documentation.
     */
    def documentation = "http://budjb.github.io/grails-async-tasks/doc/manual/index.html"

    /**
     * License.
     */
    def license = "APACHE"

    /**
     * Location of the plugin's issue tracker.
     */
    def issueManagement = [system: 'GITHUB', url: 'https://github.com/budjb/grails-async-tasks/issues']

    /**
     * Online location of the plugin's browseable source code.
     */
    def scm = [url: 'https://github.com/budjb/grails-async-tasks']
}
