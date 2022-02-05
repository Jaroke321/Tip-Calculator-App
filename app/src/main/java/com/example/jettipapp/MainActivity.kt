package com.example.jettipapp

import android.os.Bundle
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jettipapp.components.InputField
import com.example.jettipapp.ui.theme.JetTipAppTheme
import com.example.jettipapp.widgets.RoundIconButton
import java.lang.Math.round

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
        .padding(20.dp)
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

    // Holds the state of the slider
    val sliderPositionState = remember {
        mutableStateOf(0.2f)
    }

    // Keyboard controller to show or hide keyboard
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = Modifier.padding(3.dp)) {

        TopHeader()

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

                // Tip Row
                Row(modifier = Modifier.padding(vertical = 12.dp)) {

                    Text(text = "Tip", modifier = Modifier.align(alignment = Alignment.CenterVertically))
                    Spacer(modifier = Modifier.width(200.dp))

                    // Calculate the total tip amount
                    var tip = 0.0f
                    // Change the tip if there is a valid value in the bill input box
                    if (validState) {
                        tip = totalBillState.value.toFloat() * sliderPositionState.value
                    }

                    // Display the tip amount
                    Text(text = "$%.2f".format(tip))

                } // End Tip Row

                // Column for Percent and slider
                Column(verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {

                    // Calculate the percent value of the slider position
                    val percent = (sliderPositionState.value * 100).toInt()

                    Text(text = "$percent%")

                    Spacer(modifier = Modifier.height(14.dp))

                    // Slider
                    Slider(value = sliderPositionState.value,
                        steps = 19,
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                        onValueChange = { newVal ->
                            sliderPositionState.value = newVal
                        })

                } // End Slider column

            } // end of main column
        }

    }

}

//@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetTipAppTheme {
        myApp {
            TopHeader()
            MainContent()
        }
    }
}