package com.ucb.framework.mappers

import com.ucb.framework.dto.UserDto
import com.ucb.framework.persistence.entities.UserEntity

fun com.ucb.framework.dto.UserDto.toEntity(): UserEntity {
    return UserEntity(
        id = this.id,
        name = this.name,
        email = this.email
    )
}

fun UserEntity.toDto(): com.ucb.framework.dto.UserDto {
    return com.ucb.framework.dto.UserDto(
        id = this.id,
        name = this.name,
        email = this.email
    )
}