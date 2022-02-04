package com.example.jettipapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jettipapp.components.InputField
import com.example.jettipapp.ui.theme.JetTipAppTheme
import com.example.jettipapp.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            myApp {
                //TopHeader()
                MainContent()
            }
        }
    }
}

@Composable
fun myApp(content: @Composable () -> Unit) {

    JetTipAppTheme {
        Surface(color = MaterialTheme.colors.background) {
            content()
        }
    }
}

@Preview
@Composable
fun TopHeader(totalPerPerson: Double = 321.00) {
    
    Surface(modifier = Modifier
        .fillMaxWidth()
        .height(150.dp)
        .clip(shape = RoundedCornerShape(corner = CornerSize(12.dp))),
        color = Color(0xFFE9D7F7)) {

        // Inside the Top Header value surface

        // Create a Column to stack the text inside of top header
        Column(modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {

            // Inside of the column

            val total = "%.2f".format(totalPerPerson)  // Format the incoming value
            // Create the two rows holding money values
            Text(text = "Total per Person",
                style = MaterialTheme.typography.h5)

            Text(text = "$ $total",
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.ExtraBold)

        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun MainContent() {

    BillForm() { billAmount ->

        // Pass in function for on value change in the bill form

    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(modifier: Modifier = Modifier, onValChange: (String) -> Unit = {}) {

    // Holds the cost of the bill
    val totalBillState = remember {
        mutableStateOf("")
    }

    // Holds whether or not there is data in the input field
    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }

    // Holds the number of people that split the bill
    val splitNumber = remember {
        mutableStateOf(1)
    }

    // Keyboard controller to show or hide keyboard
    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(modifier = Modifier
        .padding(2.dp)
        .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        border = BorderStroke(width = 1.dp, color = Color.LightGray)) {

        Column(modifier = Modifier.padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,) {

            // Inside of bottom column
            InputField(valueState = totalBillState,
                labelId = "Enter Bill",
                enabled = true,
                isSingleLine = true,
                onAction = KeyboardActions {
                    if (!validState) return@KeyboardActions

                    onValChange(totalBillState.value.trim())

                    // Value has changed
                    keyboardController?.hide()
                })

            // Once bill amount is entered, the rest of the UI must be rendered
            if (validState) {
                // Create Row for the number of splits
                Row(modifier = Modifier.padding(3.dp),
                    horizontalArrangement = Arrangement.Start) {
                    
                    Text(
                        text = "Split",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )

                    Spacer(modifier = Modifier.width(120.dp))

                    // Row holding the plus and minus buttons
                    Row(
                        modifier = Modifier.padding(horizontal = 3.dp),
                        horizontalArrangement = Arrangement.End) {

                        // Minus Button
                        RoundIconButton(modifier = Modifier,
                            imageVector = Icons.Default.Remove,
                            onClick = {
                                // Only subtract if the split number is 1 or more
                                if (splitNumber.value >= 2) {
                                    splitNumber.value -= 1
                                }

                            }) // decrement the split number value

                        // Text holding the number of people for bill
                        Text(text = splitNumber.value.toString(),
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 9.dp, end = 9.dp))

                        // Plus Button
                        RoundIconButton(modifier = Modifier,
                            imageVector = Icons.Default.Add,
                            onClick = { splitNumber.value += 1 }) // Increment the split number value

                    } // End plus and minus row
                    
                } // End Split row

            } else {
                Box() {}
            }

        }
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetTipAppTheme {
        myApp {
            TopHeader()
            MainContent()
        }
    }
}