package com.budzilla.model

import javax.persistence.*

@Entity
class UserRole (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long?=null,
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    var user: User,
    @Column
    var role: Role
)
