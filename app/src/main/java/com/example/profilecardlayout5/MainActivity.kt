@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.profilecardlayout5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme{
                UserApplication()
            }
        }
    }
}


@Composable
fun UserApplication(userProfiles:List<UserProfile> = userProfileList){
    val navController= rememberNavController()
    NavHost(navController=navController,startDestination="users_list"){
        composable("users_list"){
            UserListScreen(userProfiles,navController)
        }
        composable(
            route="user_details/{userId}",
            arguments = listOf(navArgument("userId"){
                type=NavType.IntType
            })
        ){navBackStackEntry->
            UserProfileDetailScreen(navBackStackEntry.arguments!!.getInt("userId"),navController)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(userProfiles:List<UserProfile>,navController:NavHostController?) {
    Scaffold(topBar = { AppBar(title = "User list",
        icon = Icons.Default.Home){}}) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn {
                items(userProfiles){ userProfile->
                    profileCard(userProfile=userProfile){
                        navController?.navigate("user_details/${userProfile.id}")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileDetailScreen(userId:Int,navController:NavHostController?) {
    val userProfile= userProfileList.first{
        userProfile -> userId == userProfile.id
    }
    Scaffold(topBar = { AppBar(title = "User profile detail",
        icon = Icons.Default.ArrowBack){
        navController?.popBackStack()
    }}) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfilePicture(userProfile.pictureUrl,userProfile.status,240.dp)
            ProfileContent(userProfile.name,userProfile.status,Alignment.CenterHorizontally)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(title:String,icon:ImageVector,iconClickAction:()->Unit){
    TopAppBar(title = { Text(title,
        color = Color.White) },
        navigationIcon = {
            Icon(imageVector = icon,
                title,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .clickable { iconClickAction.invoke() },
                tint = Color.White)
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Cyan))
}

@Composable
fun profileCard(userProfile:UserProfile,clickAction:()->Unit){

    Card(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 4.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.Top)
            .clickable { clickAction.invoke() },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = CutCornerShape(topEnd = 24.dp)
    ){
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start) {
            ProfilePicture(userProfile.pictureUrl,userProfile.status,72.dp)
            ProfileContent(userProfile.name,userProfile.status,Alignment.Start)
        }
    }
 }

@Composable
fun ProfilePicture(drawableId:String,onLineStatus:Boolean,imageSize: Dp){
    Card(shape = CircleShape,
        border = BorderStroke(width = 2.dp,
        color = if (onLineStatus) colorResource(id = R.color.light_green) else Color.Red),
        modifier = Modifier.padding(16.dp),
        elevation =CardDefaults.cardElevation(
            defaultElevation = 4.dp)
    ) {
        
        AsyncImage(
            model = drawableId,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(imageSize),
            contentDescription = null,
        )
    }
}
@Composable
fun ProfileContent(userName:String,onLineStatus:Boolean,alighment:Alignment.Horizontal){
    Column(modifier = Modifier
        .padding(8.dp),
        horizontalAlignment =alighment) {
        Text(
            text = userName,
            style = MaterialTheme.typography.headlineMedium)
        CompositionLocalProvider(LocalContentColor provides LocalContentColor.current.copy(alpha = 0.4f)) {
            Text(text = if(onLineStatus)"Active now"
                else "Offline", style = MaterialTheme.typography.bodyLarge)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun UserProfileDetailReview() {
    MaterialTheme{
        UserProfileDetailScreen(userId = 0,null)
    }
}
@Preview(showBackground = true)
@Composable
fun UserListPreview() {
    MaterialTheme{
        UserListScreen(userProfileList,null)
    }
}