
# Tokenizer Android SDK

## 1. Table of contents
- [1. Table of contents](#table-of-contents)
- [2. Overview](#overview)
- [3. Getting started](#getting-started)
- [4. Collect](#collect)
- [5. Revelaer](#revealer)

## 2. Overview
The Astropay Tokenizer SDK is a powerful tool designed to facilitate easy integration of AstroPay's secure payment solutions into your Android applications. This SDK provides a way to tokenize and reveal sensitive payment information, ensuring that your transactions are both secure and efficient.

## 3. Getting Started
* **Proyecto demo:** - AP Tokenize Demo
* **Package:** - com.aptokenizer.tokenizer
* **Folders:**
```gradle
/data
	/interceptors
	/models
	/repositories

/domain
	/actions
	/models
	/repositories

/views
	/compose
	/core
	/models
	/system
```

##### SDK Initializer
- `TokenizerConfig.kt` (singleton)

##### Entry points:

- `TokenRevealer.kt`
- `TokenCollect.kt`

### 3.1 Dependencies:

| **Dependency** | **Version** |
| --- | --- |
| *Min SDK* | 21 |
| *Target SDK* | 34 |
| *org.jetbrains.kotlin:kotlin-gradle-plugin* | 1.9.0 |
| *androidx.appcompat:appcompat* | 1.7.0 |
| *com.squareup.okhttp3:okhttp* | 4.12.0 |
| *com.squareup.okhttp3:logging-interceptor* | 4.12.0 |
| *com.squareup.retrofit2:converter-gson* | 2.9.0 |

##### If you use compose in your project:

| **Dependency** | **Version** |
| --- | --- |
| *androidx.compose.material* | 1.12.0 |
| *androidx.compose.compose-bom* | 2024.06.00 |
| *androidx.activity:activity-compose* | 1.9.1 |

### 3.2 How to integrate:

In the project level build.gradle file add the accesses provided by us (user and token) to the maven repository:

```kotlin
maven {
    url = uri("https://maven.pkg.github.com/astropay-it/tokenizer-android")
    credentials {
        username = "SDK_ANDROID_USER_ARTIFACT"
        password = "SDK_ANDROID_TOKEN_ARTIFACT")
    }
}
```

To use the **SDK** in your project you just need to include the following maven dependency in the root of the project in to your `build.gradle` file at the module level and then rebuild project:

```kotlin
implementation com.aptokenizer.tokenizer:tokenizer:<version>
```

`<version>` would be the latest available version of the Tokenizer SDK:

---
## 4. Collect

### 4.1 Table of contents

- [4.2 Initialization](#initialization)
- [4.2 SDK functions](#sdk-functions)
- [4.3 Components](#components)
    - [4.3.2 For Jetpack Compose](#for-jetpack-compose)
    - [4.3.3. For Android Views](#for-android-views)
- [Instructions](#instructions)

### 4.2 Initialization

Initialize the SDK by calling the application-level `init()` function in your app:

```kotlin
open class YourApplication {
   override fun onCreate() {
      ...
      TokenizerConfig.init(
         environment = TokenRevealer.Environment.Sandbox,
         seeLogs = true
      )
   }
	...
}
```
💡Use the Sandbox environment for testing only. We recommend not allowing logs in the production release of the app.

### 4.3 SDK Functions

**TokenizerConfig:**

- **`init(environment: String, seeLogs: Boolean)`** → Initializes the SDK dependencies, receiving as parameters: the environment and whether or not logs should be recorded in the console.

**TokenCollect:**

- **`setAccessToken(accessToken: String)`** → sets a valid authentication token to consume SDK functions.
- **`clearData()` → Allows you to limit subscribed and revealed data**
- **`collect(trResult: (TRResult)`** → takes the data from the list of fields registered with the respective instance and sends them to be tokenized

### 4.4 Components

##### **4.4.1 For Jetpack Compose**

* ##### **TRTextField(..)** → Composable that allows the entry of data to be collected

**Mandatory parameters:**

1. **`tokenCollect: TokenCollect`** the instance that will be listening to the information entered by the user
2. **`fieldName: String`** is an identifier for the field, with which you can then access the respective token in the returned list.

**All Attributes:**

| **Attributes** | **Type** | **Default** | **Description** |
| --- | --- | --- | --- |
| `modifier` | `Modifier` | Modifier | Modifiers allow you to decorate or augment a composable. |
| `tokenCollect` | `TokenCollect` | - | It is the instance that will be listening to the information entered by the user |
| `fieldName` | `String` | - | It is an identifier for the field, with which you can then access the respective token in the returned list |
| `minLength` | `~~Int?~~` | null | Defines the minimum number of characters that the field could receive. |
| `maxLength` | `Int?` | null | Defines the maximum number of characters that the field could receive |
| `onFieldStateChange` | `((FieldState) -> Unit)?` | null | Lambda function with which you can listen to some properties related to the value entered in the field|
| `enabled` | `Boolean` | true | Controls the enabled state of the TextField. When false, the text field will be neither editable nor focusable, the input of the text field will not be selectable. Visually, the text field will appear in a disabled state, reflecting its inactive status in the UI. |
| `readOnly` | `Boolean` | false | Controls the editable state of the TextField. When true, the text field can’t be modified. However, a user can focus it and copy text from it. Read-only text fields are usually used to display pre-filled forms that user can’t edit. |
| `showBrand` | `Boolean` | false | When true, if the input type is `CARD_NUMBER` it will show the card’s brand image as a trailing icon. |
| `textStyle` | `TextStyle` | LocalTextStyle.current | The style to be applied to the input text. The default textStyle uses the LocalTextStyle defined by the theme. |
| `label` | `@Composable (() -> Unit)?` | null | Optional label to be displayed inside the text field container. |
| `placeholder` | `@Composable (() -> Unit)?` | null | Optional placeholder to be displayed when the text field is focused and the input text is empty. The default text style for internal Text is Typography.subtitle1 |
| `leadingIcon` | `@Composable (() -> Unit)?` | null | Optional leading icon to be displayed at the beginning of the text field container. |
| `trailingIcon` | `@Composable (() -> Unit)?` | null | Optional trailing icon to be displayed at the end of the text field container. |
| `isError` | `Boolean` | false | Indicates if the text field's current value is in error. If set to true, the label, bottom indicator and trailing icon by default will be displayed in error color |
| `textInputType` | `TextInputType` | TextInputType.NONE | Used to define the expected type of input in the text field. Available options are `CARD_NUMBER`, `PASSWORD`, `EXPIRATION_DATE`, `CVV`, `NONE` |
| `keyboardOptions` | `KeyboardOptions` | KeyboardOptions.Default | Software keyboard options that contains configuration such as KeyboardType and ImeAction. |
| `keyboardActions` | `KeyboardActions` | KeyboardActions.Default | When the input service emits an IME action, the corresponding callback is called. Note that this IME action may be different from what you specified in KeyboardOptions.imeAction. |
| `singleLine` | `Boolean` | true | When set to true, this text field becomes a single horizontally scrolling text field instead of wrapping onto multiple lines. The keyboard will be informed to not show the return key as the ImeAction. Note that maxLines parameter will be ignored as the maxLines attribute will be automatically set to 1. |
| `maxLines` | `Int` | Int.MAX_VALUE | The maximum height in terms of maximum number of visible lines. It is required that 1 <= minLines <= maxLines. This parameter is ignored when singleLine is true. |
| `interactionSource` | `MutableInteractionSource` | remember { MutableInteractionSource() | The MutableInteractionSource representing the stream of Interactions for this TextField. You can create and pass in your own remembered MutableInteractionSource if you want to observe Interactions and customize the appearance / behavior of this TextField in different Interactions. |
| `shape` | `Shape` | TextFieldDefaults.TextFieldShape | The shape of the text field's container. |
| `colors` | `TextFieldColors` | TextFieldDefaults.textFieldColors() | TextFieldColors that will be used to resolve color of the text, content (including label, placeholder, leading and trailing icons, indicator line) and background for this text field in different states. See TextFieldDefaults. textFieldColors |
| `regexRulesValidation` | `List<RegexRuleValidation>?` | null | List with the rules for the necessary validations that must be applied to the value entered by the user. Each item in the list must contain a pattern and a message if the validation is not met. The result will be delivered in 2 points, one in the onFieldStateChange lambda function, and another as a response in the collect() function. |
| `volatile` | `Boolean` | false | Allows to notify how the field is persisted in the database on the backend side |
| `enableTokenization` | `Boolean` | true | Allows to set tokenization feature to false if desired. |
| `isRequired` | `Boolean` | false | Sets the text field as required which is useful for validation purposes. |
| `onScrollToPosition` | `@Composable ((yPosition: Int) -> Unit)?` | null | Allows to scroll to position based on the `yPosition` when the text field is focused. |

##### **4.4.2 For Android Views**

* ##### **TREditText** → TextInputEditText that allows the entry of data to be collected.

**Mandatory attributes:**

1. **`tokenCollect: TokenCollect`** the instance that will be listening to the information entered by the user
2. **`fieldName: String`** is an identifier for the field, with which you can then access the respective token in the returned list.

**All Attributes:**

| **Attributes** | **Type** | **Description** |
| --- | --- | --- |
| `textAppearance` | `@StyleRes styleId: Int` | Sets the text appearance from the specified style resource. |
| `hint` | `text: CharSequence?` | Sets the text to be displayed when the text of the TextView is empty. Null means to use the normal empty text. The hint does not currently participate in determining the size of the view. |
| `hintTextColor` | `@ColorInt color: Int` | Allows you to change the color of the text that will be displayed while the data is not revealed. |
| `fieldName` | `String` | It is an identifier for the field, with which you can then access the respective token in the returned list |
| `textColor` | `@ColorInt color: Int` | Allows you to change the font color of the element |
| `textSize` | `size: Float` | Allows you to set a specific font size |
| `singleLine` | `singleLine: Boolean` | Determines whether the field should be displayed on a single line |
| `fontFamily` | `typeface: Typeface` | Allows you to set a specific font family |
| `textAlign` | `TextAlign?` | Allows you to set the alignment for the text |
| `letterSpacing` | `spacing: Float` | Sets text letter-spacing in em units. |
| `android:gravity` | `gravity: Int` | Sets the horizontal alignment of the text and the vertical gravity that will be used when there is extra space in the TextView beyond what is required for the text itself. |
| `android:inputType` | `inputType: Int` | Set the type of the content with a constant as defined for EditorInfo.inputType. |
| `android:textStyle` | `tf: Typeface?, style: Int` | Style configuration for the text such as color, font, line height etc. |
| `maxLength` | `Int` | The maximum number of characters allowed for the field |
| `volatileField` | `Boolean` | Allows to notify how the field is persisted in the database on the backend side |
| `enableTokenization` | `Boolean` | True by default, it allows to set tokenization feature to false if desired. |

**Some important functions:**

- `setOnFieldStateChangeListener(onFieldStateChangeListener: OnFieldStateChangeListener)`: Function with callback that allows listening to some properties related to the value entered in the field

```kotlin
contentLength
hasFocus
isEmpty
isValid
regexRuleValidationResult
```

- `setTokenCollect(tokenCollect: TokenCollect)`: The instance that will be listening to the information entered by the user
- `setTextInputType(textInputType: TextInputType)`: If you want to apply a mask to group the numbers entered by the user into 4 digits, you can use the type: `TextInputType.CARD_NUMBER`. There are other available options such as `PASSWORD`, `EXPIRATION_DATE`, `CVV`.

### 4.5 Instructions

Create the composition views needed for tokenization, one for each data to be tokenized, for example:

```kotlin
TRTextField(
    tokenCollect = tokenCollect,
    modifier = Modifier
        .fillMaxWidth(),
    fieldName = cardFieldName,
    enabled = tokenSaved,
    maxLength = 16,
    keyboardOptions = KeyboardOptions.Default.copy(
        keyboardType = KeyboardType.Number
    ),
    onFieldStateChange = { fieldState ->
        lengthCardNumber = fieldState.contentLength
    },
    textInputType = TextInputType.PASSWORD,
    regexRulesValidation = listOf(
        RegexRuleValidation(
            pattern = "(\\d{4})(?=\\d)",
            errorMessage = "Neither sequential nor repeated digits are allowed."
        )
    ),
    placeholder = {
        Text(
            text = "Enter the 16 digits of the card"
        )
    }
)
```

Or create the Android View in your XML file, example:

```xml
<com.aptokenizer.tokenrevealer.views.system.TREditText
        android:id="@+id/text_number_card_layout"
        style="@style/Theme.APTokenizeDemo"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginStart="16dp"
        android:layout_marginTop="16dp"
        android:layout_marginEnd="32dp"
        android:background="@drawable/tr_edit_text_bg"
        android:hint="@string/card_digits_hint"
        android:inputType="number"
        android:paddingHorizontal="4dp"
        android:paddingVertical="6dp"
        android:textColor="@color/white"
        app:enabled="false"
        app:fieldName="number"
        app:hint="@string/card_digits_hint"
        app:hintTextColor="@color/text_muted"
        app:textColor="@color/white"
        app:textSize="16sp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintVertical_bias="0.0" />

```

```kotlin
val cardTextView = findViewById<TREditText>(R.id.card_edit_text)
```

**Create the TokenCollect instance:**

You need to create the TokenCollect instance for the respective flow of your application in which you will tokenize the respective data..

```kotlin
val tokenCollect = TokenCollect()
```

**Set a valid authorization token:**

Set a valid authorization token in the SDK (tokenCollect.setAccessToken) to be able to use the data tokenized functions. Generally it will be your backend team who provides it to you: see backend documentation [here](https://www.notion.so/Guidelines-for-Merchant-and-Card-Issuers-eba6790d685b44be90b16fae1447cc38?pvs=21).

```kotlin
tokenCollect.setAccessToken("authorizationToken")
```

You can use the following [site](https://dinochiesa.github.io/jwt/) to generate **test jwt tokens** by providing the data provided to your backend team by our team.

**Collect the respective data:**

Now you just need to call the tokenCollect.collect() function that will tokenize all the fields created, to know the result, you must pass it as a parameter to a lambda function, like this:

```kotlin
tokenCollect.collect { result ->
    cardToken = String()
    pinToken = String()
    when (result) {
        is TokenCollect.TRResult.Success -> {
            cardToken = result.listTokenizedData[cardFieldName] ?: String()
            pinToken = result.listTokenizedData[pinFieldName] ?: String()
        }

        is TokenCollect.TRResult.Error -> {
            var errorMessage = result.error
            if (result.message.isNotEmpty()) errorMessage = errorMessage.plus(" - ${result.message}")
            showMessage(errorMessage)
        }
        is TokenCollect.TRResult.NetworkError -> showMessage("Network Error")
        is TokenCollect.TRResult.InvalidToken -> showMessage("Invalid Token")
        is TokenCollect.TRResult.ValueNoValid -> showMessage(result.errorMessage)
    }
    loading = false
}
```

##### **For Android Views:**

It is important that you register the TokenCollect instance that will listen for changes in the respective text field for it to work properly:

```kotlin
tREditText.setTokenCollect(tokenCollect)
```

Don't forget to define a unique fieldName for each field in your form, with this you can retrieve the respective token from the list provided.

---
## 5. Revealer

### Table of contents

- [5.1 Initialization](#initialization)
- [5.2 SDK functions](#sfk-functions)
- [5.3 Components](https://www.notion.so/Revealer-120ad6d068a58097aa8fe25014166966?pvs=21)
    - [5.3.1 For Jetpack Compose](#for-jetpack-compose)
    - [5.3.2 For Android Views](#for-android-views)
- [5.4 Instructions](#instructions)

### 5.1 Initialization

Initialize the SDK by calling the application-level `init()` function in your app:

```kotlin
open class YourApplication {
   override fun onCreate() {
      ...
      TokenizerConfig.init(
         environment = TokenRevealer.Environment.Sandbox,
         seeLogs = true
      )
   }
	...
}
```

💡Use the Sandbox environment for testing only. We recommend not allowing logs in the production release of the app.

### 5.2 SDK Functions

**TokenizerConfig:**

- **`init(environment: String, seeLogs: Boolean)`** → Initializes the SDK dependencies, receiving as parameters: the environment and whether or not logs should be recorded in the console.

**TokenRevealer:**

- **`subscribe(contentPath: String, token: String)`** → subscribes the field or fields that require to be revealed.
- **`unsubscribe(contentPath: String)`** → Removes the indicated field from the list of tokens to reveal.
- **`setAccessToken(accessToken: String)`** → sets a valid authentication token to consume SDK functions.
- **`reveal(trResult: (TRResult))`** → takes the list of subscribed fields, reveals them, and renders them to the corresponding view.
- **`copy(context: Context, contentPath: String)`** → function that allows you to copy the corresponding revealed data to the device's clipboard.
- **`clearData()`** → Allows you to limit subscribed and revealed data

### 5.3 Components

##### **5.3.1 For Jetpack Compose**

* ##### **TRText(..)** → Composable that renders the revealed data.

**Mandatory parameters:**

1. **`tokenRevealer: TokenRevealer`** the instance in which you are subscribing to the tokens to be revealed
2. **`hintText`** which allows you to indicate the text to be displayed while the data has not been revealed
3. **`contentPath`** is the key that must match the token subscribed with the `subscribe(...)` function

**Optional attributes:**

1. **`showDataRevealed`** that allows you to show or hide the revealed data. And others to customize the view.

**All Attributes:**

| **Attributes** | **Type** | **Default** | **Description** |
| --- | --- | --- | --- |
| `modifier` | `Modifier` | `Modifier` | Modifiers allow you to decorate or augment a composable. |
| `tokenRevealer` | `TokenRevealer` | - | It is the instance in which you are subscribing to the tokens to be revealed |
| `hintText` | `String` | - | It is the text that will be displayed while the data is not revealed. |
| `contentPath` | `String` | - | Identification key of the respective token that will be displayed above this view. |
| `showDataRevealed` | `Boolean` | `false` | Attribute that allows hiding or showing previously revealed data. |
| `color` | `Color` | `Color.Unspecified` | Allows you to change the font color of the element |
| `fontSize` | `TextUnit` | `TextUnit.Unspecified` | Allows you to set a specific font size |
| `fontWeight` | `FontWeight?` | `null` | Allows you to set a specific font weight |
| `fontFamily` | `FontFamily?` | `FontFamily.Default` | Allows you to set a specific font family |
| `textAlign` | `TextAlign?` | `null` | Allows you to set the alignment for the text |
| `lineHeight` | `TextUnit` | `TextUnit.Unspecified` | Allows setting a specific line height |
| `overflow` | `TextOverflow` | `TextOverflow.Clip` | Allows you to set a specific Text Overflow |
| `softWrap` | `Boolean` | `true` | Whether the text should break at soft line breaks. |
| `maxLines` | `Int` | `Int.MAX_VALUE` | An optional maximum number of lines for the text to span, wrapping if necessary. |
| `onTextLayout` | `(TextLayoutResult) -> Unit` | `{}` | Callback that is executed when a new text layout is calculated. |
| `style` | `TextStyle` | `TextStyle.Default` | Style configuration for the text such as color, font, line height etc. |
| `regexReplaceField` | `RegexReplaceField?` | `null` | This configuration consists of two key components: The **pattern**, which represents the regular expression (regex) itself. It defines the sequence of characters used to search within a text string for matching elements. And the **replacement**, which defines what the matching text will be replaced with. |

##### **5.3.2 For Android Views**

* ##### **TRTextView** → TextView where the revealed data will be displayed.

**Mandatory attributes:**

1. **`hint: String`** which allows you to indicate the text to be displayed while the data has not been revealed
2. **`contentPath: String`** is the key that must match the token subscribed with the `subscribe(...)` function

**All Attributes:**

| **Attributes** | **Type** | **Description** |
| --- | --- | --- |
| `textAppearance` | `@StyleRes styleId: Int` | Modifiers allow you to decorate or augment a composable. |
| `hint` | `text: CharSequence?` | It is the text that will be displayed while the data is not revealed. |
| `hintTextColor` | `@ColorInt color: Int` | Allows you to change the color of the text that will be displayed while the data is not revealed. |
| `contentPath` | `String` | Identification key of the respective token that will be displayed above this view. |
| `textColor` | `@ColorInt color: Int` | Allows you to change the font color of the element |
| `textSize` | `size: Float` | Allows you to set a specific font size |
| `singleLine` | `singleLine: Boolean` | Determines whether the field should be displayed on a single line |
| `fontFamily` | `typeface: Typeface` | Allows you to set a specific font family |
| `textAlign` | `TextAlign?` | Allows you to set the alignment for the text |
| `letterSpacing` | `spacing: Float` | Sets text letter-spacing in em units. |
| `android:gravity` | `gravity: Int` | Sets the horizontal alignment of the text and the vertical gravity that will be used when there is extra space in the TextView beyond what is required for the text itself. |
| `android:inputType` | `inputType: Int` | Set the type of the content with a constant as defined for EditorInfo.inputType. |
| `android:textStyle` | `tf: Typeface?, style: Int` | Style configuration for the text such as color, font, line height etc. |
| `regexReplaceField` | `RegexReplaceField?` | This configuration consists of two key components: The **pattern**, which represents the regular expression (regex) itself. It defines the sequence of characters used to search within a text string for matching elements. And the **replacement**, which defines what the matching text will be replaced with. |

### 5.4 Instructions

Create the Compose view(s) needed for revelation, one for each token to be revealed, example:

```kotlin
TRText(
    tokenRevealer = tokenRevealer,
    modifier = Modifier
        .weight(1f)
        .height(35.dp),
    contentPath = contentPath,
    hintText = "• • • •  • • • •  • • • •  • • • •",
    showDataRevealed = show,
    fontSize = 18.sp,
    regexReplaceField = RegexReplaceField(
        pattern = "(\\d{4})(?=\\d)",
        replacement = "$1 "
    )
)
```

Or create the Android View in your XML file, example:

```xml
<com.aptokenizer.tokenrevealer.views.system.TRTextView
    android:id="@+id/card_text_view"
    android:layout_width="0dp"
    android:layout_height="wrap_content"
    android:layout_marginTop="16dp"
    android:layout_marginHorizontal="16dp"
    app:textSize="18sp"
    app:contentPath="number"
    app:hint="• • • •  • • • •  • • • •  • • • •"
    app:regexPattern="(\\d{4})(?=\\d)"
    app:regexReplacement="$1 "
    app:hintTextColor="@color/white"
    app:textColor="@color/white"
    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintStart_toStartOf="parent" />
```

```kotlin
Kotlin Class:
val cardTextViw = findViewById<TRTextView>(R.id.card_text_view)
```

**Create the TokenReveal instance:**

You need to create the TokenReveal instance for the respective flow of your application in which you will subscribe to the respective tokens.

```kotlin
val tokenRevealer = TokenRevealer()
```

**Set a valid authorization token:**

Set a valid JWT authorization token in the **SDK** (`TokenRevealer.setAccessToken`) to be able to use the data revealed functions. Generally it will be your backend team who provides it to you: see backend documentation [here](https://www.notion.so/Guidelines-for-Merchant-and-Card-Issuers-eba6790d685b44be90b16fae1447cc38?pvs=21).

```kotlin
TokenRevealer.setAccessToken("authorizationToken")
```

**How to generate a JWT authentication token**

Every request must be authenticated using a JWT token that is securely signed with your private key.

Since a private key is essential for signing JWT tokens, and distributing these keys directly to mobile devices poses significant security risks, we recommend implementing a back-end service dedicated to securely generating JWT tokens for your mobile applications as shown in the following diagram.

💡 You can use the following [site](https://dinochiesa.github.io/jwt/) to generate **test jwt tokens** by providing the data provided to your backend team by our team.

**Subscribe the tokens to be revealed:**

Subscribe the tokens to be revealed in the required section of your app.

```kotlin
TokenRevealer.subscribe(
   contentPath = "contentPathExample",
   token = "tok_test_dfi3uAtS02KyeoQ2ja2C7Fd8MXe84MBd123",
   textView = androidTextView // Only if you use Android Views
)
```

💡 Keep in mind that the **contentPath** is a string defined as a key to then identify the view where the subscribed token information will be revealed. The **token** to reveal is usually provided by your backend.

**Reveal the respective data:**

Now you just need to call the `TokenRevealer.reveal()` function that will reveal all the subscribed data, to know the result pass as a parameter to a lambda function, like this:

```kotlin
TokenRevealer.reveal { result ->
   when (result) {
      is TokenRevealer.TRResult.Success -> // do something

      is TokenRevealer.TRResult.Error -> {
         result.error
         // do something
      }

      is TokenRevealer.TRResult.NetworkError -> // do something

      is TokenRevealer.TRResult.InvalidToken -> // do something
   }
}
```

### 5.5 Other functions:

**Copy function:**

Use the `copy()` function to copy the revealed information to the clipboard. Keep in mind that you will never have access to the information directly from your application, this to avoid being within reach of PCI.

```kotlin
TokenRevealer.copy(context, contentPath)
```

💡 This function receives as parameters the current **context**, and the **contentPath** string that it previously subscribed.

**Unsubscribe function:**

Use the `unsubscribe()` function to remove a respective token from the token list

```kotlin
TokenRevealer.unsubscribe("contentPath")
```

💡 This function receives as a parameter the string contentPath that it previously subscribed.


## 6. Resources:

- **AP Tokenizer SDK (`tokenrevealer`)** → provides an API for interacting with the AP Tokenizer Vault
- **AP Tokenizer DEMO (`app`)** → sample application to act as the host app for testing the **SDK** during development
