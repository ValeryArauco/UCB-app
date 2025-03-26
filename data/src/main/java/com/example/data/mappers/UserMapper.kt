package com.example.data.mappers

import com.example.data.dto.UserDto
import com.example.data.persistence.entities.UserEntity



fun UserDto.toEntity(): UserEntity {
    return UserEntity(
        id = this.id,
        name = this.name,
        email = this.email,
        nombre = TODO()
    )
}

fun UserEntity.toDto(): UserDto {
    return UserDto(
        id = this.id,
        name = this.name,
        email = this.email
    )
}