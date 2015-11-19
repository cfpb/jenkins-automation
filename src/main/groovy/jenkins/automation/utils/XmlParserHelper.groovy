package jenkins.automation.utils
import groovy.util.slurpersupport.GPathResult

class XmlParserHelper {

    def processNode( Map<String, ?> map, node) {
        if (  !map[node.name()] ){
            map[node.name()] = map.getClass().newInstance()
        }
        Map<String, ?> nodeMap = map[node.name()]

        node.children().each { it ->
            if (it.children().size() == 0) {
                processLeaf( nodeMap, it)
            } else {
                processNode( nodeMap, it)
            }
        }
        nodeMap
    }

    def processLeaf(Map<String, ?> map, node) {
        if ( map[node.name()] == null) {
            map[node.name()] = node.text()
        } else {
            if (  ! (map[node.name()] instanceof List) ) {
                map[node.name()] = [ map[node.name()] ]
            }
            map[node.name()] << node.text()
        }

        map[node.name()]
    }

    def parse(String xmlString) {
        final GPathResult xml = new XmlSlurper().parseText(xmlString)
        final Map map = [ : ]

        xml.children().each {
            if ( it.children().size() == 0 ){
                processLeaf map, it
            } else {
                processNode map, it
            }
        }

        map
    }
}

