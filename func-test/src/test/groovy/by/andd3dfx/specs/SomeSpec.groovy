package by.andd3dfx.specs

import groovyx.net.http.HttpResponseException
import org.apache.commons.lang.RandomStringUtils
import spock.lang.Specification

import static by.andd3dfx.configs.Configuration.restClient

class SomeSpec extends Specification {

    def 'Read all articles'() {
        when: 'get all articles'
        def response = restClient.get(path: '/api/v1/articles')

        then: 'server returns 200 code (ok)'
        assert response.status == 200
        and: 'got at least 10 records'
        assert response.responseData.content.size() >= 10
    }

    def 'Read all articles using pagination'() {
        when: 'get all articles'
        def response = restClient.get(
                path: '/api/v1/articles',
                query: [size: '2', page: '4', sort: 'author,DESC']
        )

        then: 'server returns 200 code (ok)'
        assert response.status == 200
        and: 'got 2 records'
        assert response.responseData.content.size() == 2
        assert response.responseData.content[0].title == 'Отечник'
    }

    def 'Read particular article'() {
        when: 'get particular article'
        def getResponse = restClient.get(path: '/api/v1/articles/' + id)

        then: 'server returns 200 code (ok)'
        assert getResponse.status == 200
        and: 'got expected article'
        assert getResponse.responseData.title == title
        assert getResponse.responseData.summary == summary
        assert getResponse.responseData.author == author

        where:
        id | title                              | summary                          | author
        1  | 'Игрок'                            | 'Рассказ о страсти игромании'    | 'Федор Достоевский'
        8  | 'Сила Божия и немощь человеческая' | 'Жизнеописание игумена Феодосия' | 'Сергей Нилус'
        9  | 'Отечник'                          | 'Цитаты Святых Отцов'            | 'Игнатий Брянчанинов'
        10 | 'Душеполезные поучения'            | 'Азбука духовной жизни'          | 'Авва Дорофей'
    }

    def 'Create an article'() {
        when: 'create an article'
        def response = restClient.post(
                path: '/api/v1/articles',
                body: ['title': 'Some new title', 'summary': 'Bla-bla summary', 'text': 'BomBiBom', 'author': 'Gogol'],
                requestContentType: 'application/json'
        )

        then: 'got 201 status'
        assert response.status == 201
        and: 'got created article details in response'
        assert response.responseData.title == 'Some new title'
        assert response.responseData.summary == 'Bla-bla summary'
        assert response.responseData.text == 'BomBiBom'

        cleanup:
        restClient.delete(path: '/api/v1/articles/' + response.responseData.id)
    }

    def 'Delete an article'() {
        setup: 'create an article'
        def response = restClient.post(
                path: '/api/v1/articles',
                body: [
                        title  : generateRandomString(10),
                        summary: generateRandomString(10),
                        text   : 'Weird text',
                        author : 'Pushkin Hlopushkin'
                ],
                requestContentType: 'application/json'
        )
        and: 'got 201 status'
        assert response.status == 201
        def id = response.responseData.id

        when: 'delete particular article'
        def deleteResponse = restClient.delete(path: '/api/v1/articles/' + id)

        then: 'server returns 204 code'
        assert deleteResponse.status == 204

        and: 'couldn\'t get deleted article by id, got 404 error instead'
        try {
            restClient.get(path: '/api/v1/articles/' + id)
            throw new RuntimeException("Should not found deleted article")
        } catch (HttpResponseException hre) {
            assert hre.statusCode == 404
        }
    }

    def 'Update an article'() {
        when: 'update title of an article with id=2'
        def newTitle = generateRandomString(10)
        def response = restClient.patch(
                path: '/api/v1/articles/2',
                body: [title: newTitle],
                requestContentType: 'application/json'
        )

        then: 'got 200 status'
        assert response.status == 200

        and: 'read an article'
        def getResponse = restClient.get(path: '/api/v1/articles/2')
        and: 'got 200 status'
        assert getResponse.status == 200
        and: 'got article title equals to new value'
        assert getResponse.responseData.title == newTitle
    }

    private String generateRandomString(int count) {
        RandomStringUtils.random(count, true, true)
    }
}
