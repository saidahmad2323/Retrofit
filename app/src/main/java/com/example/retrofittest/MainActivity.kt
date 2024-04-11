package com.example.retrofittest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://rickandmortyapi.com/api/"

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

private val apiService = retrofit.create(ApiService::class.java)

class MainActivity : ComponentActivity() {
    private val characterRepository: CharacterRepository by lazy {
        CharacterRepositoryImpl(apiService)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                RickAndMortyScreens(characterRepository)
            }
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun RickAndMortyScreens(apiService: CharacterRepository) {
    val state = remember{ mutableStateOf(true)}
    var character by remember { mutableStateOf<Character?>(null) }
    val random = remember{ mutableStateOf((1..100).random())}
    val currentId = remember { mutableStateOf(random.value) }
    val nextId = remember { mutableStateOf(currentId.value + 1) }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        fun loadRandomCharacter(id: Int) {
            GlobalScope.launch(Dispatchers.IO) {
                character = apiService.getCharacterById(id)
            }
        }
        fun randomChar(){
            loadRandomCharacter(random.value)
        }

        Button(onClick = {
            randomChar()
            state.value = false
        }, enabled = state.value) {
            Text("Получить персонажа рандомно")
        }
        Spacer(modifier = Modifier.height(16.dp))

        character?.let {
            Spacer(modifier = Modifier.height(16.dp))
            CharacterDetails(
                character = it,
                next = {
                    loadRandomCharacter(nextId.value)
                    currentId.value = nextId.value
                    nextId.value++
                },
                diznext = {
                    loadRandomCharacter(currentId.value - 1)
                    nextId.value = currentId.value
                    currentId.value--
                }
            )
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun CharacterDetails(
    character: Character,
    next: () -> Unit,
    diznext: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                diznext()
            }) {
                Text("Previous")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Image(
                painter = rememberImagePainter(character.image),
                contentDescription = character.name,
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = {
                next()
            }) {
                Text("Next")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Name: ${character.name}")
        Text(text = "Status: ${character.status}")
        Text(text = "Species: ${character.species}")
        Text(text = "Gender: ${character.gender}")
        Text(text = "Id: ${character.id}")
    }
}
