class AsyncTasksGrailsPlugin {
    /**
     * Plugin version.
     */
    def version = "0.0.4"

    /**
     * Title of the plugin.
     */
    def title = "Asynchronous Tasks Plugin"

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
    def grailsVersion = "2.1 > *"

    /**
     * Plugin dependencies
     */
    def dependsOn = ['hibernate': '2.0.0 > *']

    /**
     * Resources that are excluded from plugin packaging.
     */
    def pluginExcludes = [
        'grails-app/controllers/**',
        'grails-app/services/**',
        'grails-app/taglib/**',
        'grails-app/views/**',
        'web-app/**',
        'src/docs/**'
    ]

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
