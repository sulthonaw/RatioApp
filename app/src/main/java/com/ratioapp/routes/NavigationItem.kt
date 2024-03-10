package com.ratioapp.routes

sealed class NavigationItem(val route: String) {


    object Home : NavigationItem(NameScreen.HOME.name)
    object DetailPhotoUser : NavigationItem(NameScreen.DETAIL_PHOTO_USER.name)
    object DetailPhotoMe : NavigationItem(NameScreen.DETAIL_PHOTO_ME.name)
    object ProfileMe : NavigationItem(NameScreen.PROFILE_ME.name)
    object Wallet : NavigationItem(NameScreen.WALLET.name)
    object DonateAmount : NavigationItem(NameScreen.DONATE_AMOUNT.name)
    object Login : NavigationItem(NameScreen.LOGIN.name)
    object StatusMidtrans : NavigationItem(NameScreen.STATUS_MIDTRANS.name)
    object Registration : NavigationItem(NameScreen.REGISTRATION.name)
    object DetailAlbums : NavigationItem(NameScreen.DETAIL_ALBUMS.name)
    object WithDrawal : NavigationItem(NameScreen.WITHDRAWAL.name)
    object MidtransPayment : NavigationItem(NameScreen.MIDTRANS_PAYMENT.name)
    object ProfileUser : NavigationItem(NameScreen.PROFILE_USER.name)
    object Chat : NavigationItem(NameScreen.CHAT.name)
    object DetailChat : NavigationItem(NameScreen.DETAIL_CHAT.name)
    object FormAddPhoto : NavigationItem(NameScreen.FORM_CREATE_PHOTO.name)
    object EditProfile : NavigationItem(NameScreen.EDIT_PROFILE.name)
    object DetailFollow : NavigationItem(NameScreen.DETAIL_FOLLOW.name)
    object Pin : NavigationItem(NameScreen.PIN.name)
}
