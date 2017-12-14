package gorm.logical.delete

import grails.plugins.Plugin

class GormLogicalDeleteGrailsPlugin extends Plugin {

    // the version or versions of Grails the plugin is designed for
    def grailsVersion = '3.3.2 > *'
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            'grails-app/views/error.gsp'
    ]

    // TODO Fill in these fields
    def title = 'GORM Logical Delete' // Headline display name of the plugin
    def author = 'Your name'
    def authorEmail = ''
    def description = '''\
Brief summary/description of the plugin.
'''
    def profiles = ['web']

    // URL to the plugin's documentation
    def documentation = 'http://grails.org/plugin/gorm-logical-delete'

    // Extra (optional) plugin metadata

    // License: one of 'APACHE', 'GPL2', 'GPL3'
//    def license = "APACHE"

    // Details of company behind the plugin (if there is one)
//    def organization = [ name: "My Company", url: "http://www.my-company.com/" ]

    // Any additional developers beyond the author specified above.
//    def developers = [ [ name: "Joe Bloggs", email: "joe@bloggs.net" ]]

    // Location of the plugin's issue tracker.
//    def issueManagement = [ system: "JIRA", url: "http://jira.grails.org/browse/GPMYPLUGIN" ]

    // Online location of the plugin's browseable source code.
//    def scm = [ url: "http://svn.codehaus.org/grails-plugins/" ]
}
