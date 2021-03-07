package com.example.contactsscroll.data.objects

class Contact {
    var id: Int = 0
    var contact: String = ""

    constructor(contact: String) : this(0, contact)

    constructor(id: Int, contact: String) {
        this.id = id
        this.contact = contact
    }
}