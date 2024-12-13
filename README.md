<h1 align="center"> Loom: A Metaphor for Dynamic State Management </h1>
<p align="center">
<a href="https://reactnative.dev/docs/contributing">
    <img src="https://img.shields.io/badge/PRs-welcome-brightgreen.svg" alt="PRs welcome!" />
</a>
<br>
<span>The name <b>Loom</b> draws inspiration from the traditional tool used for weaving fabric, symbolizing the process of integrating and managing data streams seamlessly in modern applications. This analogy is especially fitting for a system responsible for handling dynamic state updates, like refreshing UI states or fetching new data.</span>
<br>
</p>

![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/colored.png)

## 👇 Overview

#### Why "Loom"?

 - **Integration**: A loom weaves threads into a cohesive fabric, much like how the Loom system integrates multiple data streams into a single, unified state.

- **Precision and Control**: Just as a loom controls the pattern and flow of threads, the Loom manages the timing and structure of state updates, ensuring consistency and avoiding unnecessary re-fetches.

- **Scalability**: Looms can handle complex patterns with many threads, mirroring how the Loom system supports diverse and complex state flows with ease.

#### Loom in State Management

The Loom acts as an orchestrator for state updates, focusing on two key aspects:

- **Initial Data Fetch** On the first subscription, Loom triggers the fetch operation to populate the state with initial data. Example: Automatically loading the home screen data on app launch.

- **Controlled Refreshing** Subsequent refresh actions are explicitly triggered by user interactions or external signals, ensuring efficient and intentional updates without redundant API calls.

#### Key Features of Loom
- **Replay-Capable Flow** The Loom uses a replayable StateFlow to retain the last emitted value, ensuring late subscribers can still access the most recent state without re-fetching data.

- **Explicit Refresh Triggers**  Allows the UI or external components to trigger updates through a refresh() function, maintaining clear separation of responsibilities.

- **Seamless Integration** Works with Kotlin's StateFlow or SharedFlow to provide reactive and declarative state management.

#### Advantages of Loom
- **Efficiency** 
    - Avoids unnecessary recomposition or redundant API calls by reusing existing data when possible.
    - Only the first subscriber triggers the initial data fetch, optimizing network usage.
    
- **Reusability**
    - Can be used across multiple ViewModels or components for consistent state handling.
    
- **Extensibility**
    - Supports adding additional layers (e.g., caching, error handling) without changing its core design.

#### Implementation

```kotlin
class Loom<T>(
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO),
    private val debounceTimeMillis: Long = DEBOUNCE,
    private val throttleTimeMillis: Long = THROTTLE,
    private val execute: suspend () -> T
) {

    private val _state = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val state: StateFlow<UiState<T>> = _state
        .streamLine(debounceTimeMillis, throttleTimeMillis)
        .onStart { emit(Unit) } // Emit the first refresh automatically
        .flatMapLatest {
            flow {
                emit(UiState.Loading)
                try {
                    val data = execute()
                    emit(UiState.Loaded(data))
                } catch (e: Exception) {
                    emit(UiState.Error(e))
                }
            }
        }
        .catch { emit(UiState.Error(it)) }
        .stateIn(scope, SharingStarted.Lazily, UiState.Init)

    fun refresh() {
        scope.launch { _state.emit(Unit) }
    }
}

private const val DEBOUNCE = 200L // Adjustable debounce period
private const val THROTTLE = 1_000L // Adjustable throttle period
private fun <T> Flow<T>.streamLine(
    debounceTimeMillis: Long = DEBOUNCE,
    throttleTimeMillis: Long = THROTTLE
): Flow<T> = this
    .debounce(debounceTimeMillis)
    // regular interval of updates where periodic updates are desired
    // regardless of how often new data is produced.
    .sample(throttleTimeMillis)

fun <T> loomIn(
    scope: CoroutineScope = CoroutineScope(Dispatchers.IO),
    debounceTimeMillis: Long = DEBOUNCE,
    throttleTimeMillis: Long = THROTTLE,
    execute: suspend () -> T
) = Loom(
    scope = scope,
    debounceTimeMillis = debounceTimeMillis,
    throttleTimeMillis = throttleTimeMillis,
    execute = execute
)
```

#### Conclusion

The Loom metaphor emphasizes the importance of structured, efficient, and scalable state management. By weaving together streams of data and providing explicit controls for refreshing, Loom ensures that the UI remains responsive, consistent, and performant. This approach not only aligns with modern reactive programming paradigms but also provides a clear and elegant mechanism for managing dynamic states.

## 🚀 How to use

Cloning the repository into a local directory and checkout the desired branch:

```
git clone git@github.com:nphausg/loom.git
git checkout master
```

## ✨ Contributing

Please feel free to contact me or make a pull request.

<a href="https://revolut.me/nphausg" target="_blank"><img src="https://www.buymeacoffee.com/assets/img/custom_images/orange_img.png" alt="nphausg" style="height: 41px !important;width: 174px !important;box-shadow: 0px 3px 2px 0px rgba(190, 190, 190, 0.5) !important;-webkit-box-shadow: 0px 3px 2px 0px rgba(190, 190, 190, 0.5) !important;" ></a>

## 👇 Author

<p>
    <a href="https://nphausg.medium.com/" target="_blank">
    <img src="https://avatars2.githubusercontent.com/u/13111806?s=400&u=f09b6160dbbe2b7eeae0aeb0ab4efac0caad57d7&v=4" width="96" height="96" alt="">
    </a>
</p>
