/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
    def author = 'Jeff Scott Brown'
    def authorEmail = 'brownj@objectcomputing.com'
    def description = '''\
GORM Logical Delete Plugin For Grails 3.
'''
    def profiles = ['web']

    // URL to the plugin's documentation
    def documentation = 'http://grails.org/plugin/gorm-logical-delete'

    def license = 'APACHE2'

    def organization = [ name: 'OCI', url: 'http://www.objectcomputing.com/' ]

    def developers = [ [ name: 'Ben Rhine', email: 'rhineb@objectcomputing.com' ],
                       [ name: 'Nirav Assar', email: 'assarn@objectcomputing.com' ]]

    def issueManagement = [ system: 'GitHub', url: 'https://github.com/grails-plugins/gorm-logical-delete/issues' ]

    def scm = [ url: 'https://github.com/grails-plugins/gorm-logical-delete' ]

    Closure doWithSpring() { { ->
        logicalDeletePreQueryListener PreQueryListener
    } }
}
