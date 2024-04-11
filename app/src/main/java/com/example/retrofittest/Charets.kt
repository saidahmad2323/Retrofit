package com.example.retrofittest


data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val image: String
)


interface CharacterUseCase {
    suspend fun getRandomCharacter(): Character
    suspend fun getNextCharacter(currentId: Int): Character
    suspend fun getPreviousCharacter(currentId: Int): Character
}


interface CharacterRepository {
    suspend fun getCharacterById(id: Int): Character
}

