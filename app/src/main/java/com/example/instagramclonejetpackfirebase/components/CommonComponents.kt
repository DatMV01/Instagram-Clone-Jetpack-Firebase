package com.example.instagramclonejetpackfirebase.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.instagramclonejetpackfirebase.viewmodel.IgViewModel

@Composable
fun ToastMessage(vm: IgViewModel): Unit {
    val notifiState = vm.popupNotification.value;
    val notifiMessage = notifiState?.getContentOrNull();

    if (notifiMessage != null) {
        Toast.makeText(LocalContext.current, notifiMessage, Toast.LENGTH_SHORT).show();
    }
}

@Composable
@Preview(showBackground = true)
fun CommonProgressSpinner(): Unit {
  Row (
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
      ,modifier = Modifier
          .fillMaxSize()
          .alpha(0.5f)
          .background(Color.LightGray)
          .clickable(enabled = false) { }){
      CircularProgressIndicator()
  }
}