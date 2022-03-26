package pl.zycienakodach.esflights.sdk.application.eventstream

import spock.lang.Specification

class EventStreamNameSpec extends Specification {

    def "is composed from category and id"() {
        given:
        def category = "CategoryName"
        def id = "stream-id"

        when:
        def eventStreamName = EventStreamName.ofCategory(category).withId(id)

        then:
        eventStreamName.category() == category
        eventStreamName.id() == id
        eventStreamName.toString() == "CategoryName_stream-id"
    }

    def "two streams with same category and id are equal"() {
        when:
        def eventStreamName1 = EventStreamName.ofCategory(cateogry1).withId(id1)
        def eventStreamName2 = EventStreamName.ofCategory(category2).withId(id2)

        then:
        (eventStreamName1 == eventStreamName2) == equal

        where:
        cateogry1   | id1   | category2   | id2   || equal
        'categoryA' | 'idA' | 'categoryB' | 'idB' || false
        'categoryA' | 'idA' | 'categoryA' | 'idB' || false
        'categoryA' | 'idA' | 'categoryB' | 'idA' || false
        'categoryA' | 'idA' | 'categoryA' | 'idA' || true
    }

}
