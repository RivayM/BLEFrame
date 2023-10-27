package com.example.bleframe.entities

interface Person {
    interface PersonDataAuth:Person{
        var id: Int?
        var firstName: String
        var lastName: String
        var email: String
        var password: String
        var authorizedApp: Boolean
    }

}
