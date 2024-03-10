package com.ratioapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.ratioapp.routes.NavigationItem
import com.ratioapp.ui.components.AuthMiddleware
import com.ratioapp.ui.screen.ChatScreen
import com.ratioapp.ui.screen.DetailAlbumsScreen
import com.ratioapp.ui.screen.DetailChatScreen
import com.ratioapp.ui.screen.DetailFollowScreen
import com.ratioapp.ui.screen.detailphoto.DetailPhotoScreen
import com.ratioapp.ui.screen.FormAddPhotoScreen
import com.ratioapp.ui.screen.FormDonateAmountScreen
import com.ratioapp.ui.screen.FormEditProfileScreen
import com.ratioapp.ui.screen.FormWithDrawal
import com.ratioapp.ui.screen.HomeScreen
import com.ratioapp.ui.screen.LoginScreen
import com.ratioapp.ui.screen.MidtransScreen
import com.ratioapp.ui.screen.PinScreen
import com.ratioapp.ui.screen.StatusMidtransScreen
import com.ratioapp.ui.screen.WalletScreen
import com.ratioapp.ui.screen.detailphoto.DetailPhotoMeScreen
import com.ratioapp.ui.screen.profile.ProfileMeScreen
import com.ratioapp.ui.screen.profile.ProfileUserScreen
import com.ratioapp.ui.screen.register.RegistrationScreen
import com.ratioapp.ui.theme.RatioAppTheme
import com.ratioapp.viewModels.MidtransViewModel
import com.ratioapp.viewModels.WithDrawalViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            RatioAppTheme(false) {
                val navController = rememberNavController()
                val midtransViewModel: MidtransViewModel = viewModel()
                val withDrawalViewModel: WithDrawalViewModel = viewModel()

                val snackbarHostState = remember {
                    SnackbarHostState()
                }

                NavHost(
                    navController = navController,
                    startDestination = NavigationItem.Home.route,
                    exitTransition = {
                        ExitTransition.None
                    },
                    enterTransition = {
                        EnterTransition.None
                    }
                ) {
                    composable(NavigationItem.Home.route) {
                        AuthMiddleware(navController = navController) {
                            HomeScreen(navController)
                        }
                    }
                    composable(NavigationItem.DetailPhotoUser.route + "/{photoId}") {
                        val photoId = it.arguments?.getString("photoId")
                        if (photoId != null)
                            AuthMiddleware(navController = navController) {
                                DetailPhotoScreen(navController, photoId)
                            }
                        else
                            CircularProgressIndicator()
                    }
                    composable(NavigationItem.ProfileMe.route) {
                        AuthMiddleware(navController = navController) {
                            ProfileMeScreen(navController)
                        }
                    }
                    composable(NavigationItem.DetailPhotoMe.route + "/{photoId}") {
                        AuthMiddleware(navController = navController) {
                            DetailPhotoMeScreen(navController)
                        }
                    }
                    composable(NavigationItem.Pin.route) {
                        AuthMiddleware(navController = navController) {
                            PinScreen(
                                navController,
                                viewModel = withDrawalViewModel,
                                snackbarHostState
                            )
                        }
                    }
                    composable(NavigationItem.EditProfile.route) {
                        AuthMiddleware(navController = navController) {
                            FormEditProfileScreen(navController)
                        }
                    }
                    composable(NavigationItem.Wallet.route) {
                        AuthMiddleware(navController = navController) {
                            WalletScreen(navController, snackbarHostState)
                        }
                    }
                    composable(NavigationItem.DonateAmount.route + "/{photoId}") {
                        AuthMiddleware(navController = navController) {
                            it.arguments?.getString("photoId")?.let { it1 ->
                                FormDonateAmountScreen(
                                    navController,
                                    photoId = it1, midtransViewModel
                                )
                            }
                        }
                    }
                    composable(NavigationItem.Login.route) {
                        LoginScreen(navController)
                    }
                    composable(NavigationItem.StatusMidtrans.route + "/{donationId}/{token}") {
                        StatusMidtransScreen(navController)
                    }
                    composable(NavigationItem.Registration.route) {
                        RegistrationScreen(navController = navController)
                    }
                    composable(route = NavigationItem.DetailAlbums.route + "/{albumId}") {
                        AuthMiddleware(navController = navController) {
                            val albumId = it.arguments?.getString("albumId")
                            if (!albumId.isNullOrEmpty())
                                AuthMiddleware(navController = navController) {
                                    DetailAlbumsScreen(
                                        navController = navController,
                                        albumId = albumId
                                    )
                                }
                        }
                    }
                    composable(NavigationItem.WithDrawal.route) {
                        AuthMiddleware(navController = navController) {
                            FormWithDrawal(
                                navController = navController,
                                viewModel = withDrawalViewModel
                            )
                        }
                    }
                    composable(NavigationItem.DetailFollow.route + "/{userId}/{type}") {
                        AuthMiddleware(navController = navController) {
                            val userId = it.arguments?.getString("userId")
                            val type = it.arguments?.getString("type")
                            if (!userId.isNullOrEmpty() && !type.isNullOrEmpty())
                                DetailFollowScreen(
                                    navController = navController,
                                    userId = userId,
                                    type = type
                                )
                        }
                    }
                    composable(NavigationItem.MidtransPayment.route) {
                        AuthMiddleware(navController = navController) {
                            MidtransScreen(navController = navController, midtransViewModel)
                        }
                    }
                    composable(NavigationItem.ProfileUser.route + "/{userId}") {
                        val userId = it.arguments?.getString("userId")
                        if (!userId.isNullOrEmpty())
                            AuthMiddleware(navController = navController) {
                                ProfileUserScreen(navController = navController, userId = userId)
                            }
                    }
                    composable(NavigationItem.Chat.route) {
                        AuthMiddleware(navController = navController) {
                            ChatScreen(navController = navController)
                        }
                    }
                    composable(NavigationItem.FormAddPhoto.route) {
                        AuthMiddleware(navController = navController) {
                            FormAddPhotoScreen(navController = navController)
                        }
                    }
                    composable(NavigationItem.DetailChat.route + "/{userId}") {
                        AuthMiddleware(navController = navController) {
                            val userId = it.arguments?.getString("userId")
                            if (!userId.isNullOrEmpty())
                                DetailChatScreen(navController = navController, userId)
                        }
                    }
                }
            }
        }
    }
}