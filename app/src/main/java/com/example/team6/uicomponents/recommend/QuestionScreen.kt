package com.example.team6.uicomponents.recommend

import android.R.attr.label
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.team6.model.Routes

@Composable
fun QuestionScreen(navController: NavHostController) {

    val steps = 5
    var currentStep by remember { mutableStateOf(0) }

    var age by remember { mutableStateOf("") }
    var importantPoint by remember { mutableStateOf("") }
    var guardianAvailable by remember { mutableStateOf("") }
    var active by remember { mutableStateOf("") }
    var nowadmission by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "아이에게 딱 맞는 유치원 찾기",
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        when (currentStep) {
            0 -> {
                Text("1. 아이의 나이는 몇 살인가요?")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it },
                    placeholder = { Text("숫자를 입력해주세요") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            1 -> {
                Text("2. 안전을 다른 요소보다 중요하게 보나요??")
                Spacer(modifier = Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    listOf("네", "아니요").forEach { answer ->
                        val isSelected = importantPoint == answer
                        Button(
                            onClick = { importantPoint = answer },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        ) {
                            Text(answer)
                        }
                    }
                }
            }

            2 -> {
                Text("3. 등하원 시 보호자 동행이 가능한가요?")
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    listOf("네", "아니요").forEach { answer ->
                        val isSelected = guardianAvailable == answer
                        Button(
                            onClick = { guardianAvailable = answer },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        ) {
                            Text(answer)
                        }
                    }
                }
            }

            3 -> {
                Text("4. 아이가 활동적인가요?")
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    listOf("네", "아니요").forEach { answer ->
                        val isSelected = active == answer
                        Button(
                            onClick = { active = answer },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        ) {
                            Text(answer)
                        }
                    }
                }
            }

            4 -> {
                Text("5. 지금 입학이 가능해야 하나요?")
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    listOf("네", "아니요").forEach { answer ->
                        val isSelected = nowadmission == answer
                        Button(
                            onClick = { nowadmission = answer },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        ) {
                            Text(answer)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        val currentValid = when (currentStep) {
            0 -> age.isNotBlank()
            1 -> importantPoint.isNotBlank()
            2 -> guardianAvailable.isNotBlank()
            3 -> active.isNotBlank()
            4 -> nowadmission.isNotBlank()
            else -> false
        }

        Button(
            onClick = {
                if (currentStep < steps - 1) {
                    currentStep++
                } else {
                    navController.navigate(
                        Routes.ResultWithArgs.createRoute(age, importantPoint, guardianAvailable, active, nowadmission)
                    ) {
                        popUpTo("recommend") { inclusive = false }
                    }
                }
            },
            enabled = currentValid,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BFFF)),
        ) {
            Text(
                text = if (currentStep == steps - 1) "결과 보기" else "다음",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}