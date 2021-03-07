package com.example.contactsscroll.data.objects

class ContactType {
    var id: Int = 0
    var contactType: String = ""
    var personId = 0
    var contactId = 0


    constructor(contactType: String, contactId: Int, personID: Int) : this(
        0,
        contactType,
        contactId,
        personID
    )

    constructor(id: Int, contactType: String, contactId: Int, personID: Int) {
        this.id = id
        this.contactType = contactType
        this.contactId = contactId
        this.personId = personID

    }
}