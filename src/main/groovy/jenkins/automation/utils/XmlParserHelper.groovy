package jenkins.automation.utils
import groovy.util.slurpersupport.GPathResult

/**
 * Utility class to aid testing and XML handling
 */
class XmlParserHelper {

    private  processNode( Map<String, ?> map, node) {
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

    private processLeaf(Map<String, ?> map, node) {
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
    /**
     * Converts XML string into a HashMap
     * very useful in xml string comparison
     * @param xmlString  XML string to be parsed
     * @return Hashmap  Hashmap of maps using node names as keys
     */
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

