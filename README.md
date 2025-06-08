### ZenZapAssignment
The app is built using Jetpack Compose with MVVM architecture for clear separation of concerns.
I use Hilt for dependency injection to manage components cleanly and improve testability. 
Retrofit with Kotlin coroutines handles API communication, and responses are cached locally using
Room to reduce unnecessary network calls. When the user types $ followed by text,
a search API is called to fetch matching stock symbols, and the app displays the first 5 results
sorted alphabetically. Selecting a stock adds it to the watchlist and clears the input for a
smoother experience. A coroutine-based timer updates stock prices and changes every 30 seconds,
and updates are automatically paused when the screen is not active. UI and state are managed
with Compose's reactive tools to ensure smooth, responsive updates.

### Assumptions:

1. **API Request Limit**:
   The main constraint is a maximum of **25 requests per day.** Once this limit is reached,
   the application will not work even with a new API key due to IP-based restrictions.
   To continue development or usage:

    * Generate a new API key.
    * Update the `secrets.properties` file with the new key.
    * Change the device’s IP address.
      The simplest way to do this is by using a free VPN service. For development, I used TunnelBear VPN.

2. **Internet Connection**:
   The application assumes that the device has an active internet connection. If no connection is available,
   the app will display an appropriate message.

3. **Theme Support**:
   The app does not support dynamic switching between Day and Night modes.
   It assumes that the device is using a light theme.

4. **No database cleanup**:
   The app does not implement any database cleanup logic. 

