package com.example.retrofittest

class CharacterRepositoryImpl(private val apiService: ApiService) : CharacterRepository {
    override suspend fun getCharacterById(id: Int): Character {
        return apiService.getCharacterById(id)
    }
}