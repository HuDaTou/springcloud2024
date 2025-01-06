package com.overthinker.cloud

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "user", schema = "cloud_2024")
open class User {
    @Id
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @javax.validation.constraints.Size(max = 50)
    @javax.validation.constraints.NotNull
    @Column(name = "username", nullable = false, length = 50)
    open var username: String? = null

    @javax.validation.constraints.Size(max = 500)
    @Column(name = "password", length = 500)
    open var password: String? = null

    @javax.validation.constraints.Size(max = 255)
    @javax.validation.constraints.NotNull
    @Column(name = "enabled", nullable = false)
    open var enabled: String? = null
}