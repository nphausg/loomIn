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

<p align="center">
<a href="https://revolut.me/nphausg" target="_blank"><img src="docs/logo/01.jpeg" alt="nphausg" style="width: 386px !important;" ></a>
</p>

## 👇 Overview

### 🎯 Why LoomFlow?

LoomFlow offers a clean, easy-to-use solution for developers handling complex asynchronous workflows. It simplifies managing state-driven operations and ensures your app performs efficiently by avoiding redundant calls and state updates.

 - **Integration**: A loom weaves threads into a cohesive fabric, much like how the Loom system integrates multiple data streams into a single, unified state.

- **Precision and Control**: Just as a loom controls the pattern and flow of threads, the Loom manages the timing and structure of state updates, ensuring consistency and avoiding unnecessary re-fetches.

- **Scalability**: Looms can handle complex patterns with many threads, mirroring how the Loom system supports diverse and complex state flows with ease.

### 🐛 Loom in State Management

The Loom acts as an orchestrator for state updates, focusing on two key aspects:

- **Initial Data Fetch** On the first subscription, Loom triggers the fetch operation to populate the state with initial data. Example: Automatically loading the home screen data on app launch.

- **Controlled Refreshing** Subsequent refresh actions are explicitly triggered by user interactions or external signals, ensuring efficient and intentional updates without redundant API calls.

## 🚀 Key Features of Loom

- **Debounce and Throttle:** Control the frequency of state updates with customizable debounce and throttle intervals.
- **StateFlow Management:** Efficiently manage state with `StateFlow`, offering a seamless reactive flow system.
- **Safe Concurrency:** Synchronize tasks using a robust locking mechanism to avoid race conditions and ensure thread safety.
- **Kotlin-centric:** Built with Kotlin and CoroutineScope, leveraging Kotlin's best features for asynchronous operations.
- **Replay-Capable Flow** The Loom uses a replayable StateFlow to retain the last emitted value, ensuring late subscribers can still access the most recent state without re-fetching data.

- **Explicit Refresh Triggers**  Allows the UI or external components to trigger updates through a refresh() function, maintaining clear separation of responsibilities.

- **Seamless Integration** Works with Kotlin's StateFlow or SharedFlow to provide reactive and declarative state management.

## 🚀 Advantages of Loom

- **Efficiency** 
    - Avoids unnecessary recomposition or redundant API calls by reusing existing data when possible.
    - Only the first subscriber triggers the initial data fetch, optimizing network usage.
    
- **Reusability**
    - Can be used across multiple ViewModels or components for consistent state handling.
    
- **Extensibility**
    - Supports adding additional layers (e.g., caching, error handling) without changing its core design.

#### Implementation

```kotlin
internal class LoomImpl<T>(
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO),
    private val debounceTimeMillis: Long = DEBOUNCE,
    private val throttleTimeMillis: Long = THROTTLE,
    private val execute: suspend (Boolean) -> T
) : Loom<T> {
    private val mutex = Mutex();
    private val _modifier = MutableSharedFlow<(T) -> T>(extraBufferCapacity = 1)
    private val _state = MutableSharedFlow<LoomSignal>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val state: StateFlow<LoomState<T>> = _state
        .streamLine(debounceTimeMillis, throttleTimeMillis)
        .onStart { emit(LoomSignal.Automatic) } // Emit the first refresh automatically
        .flatMapLatest { event ->
            flow {
                emit(LoomState.Loading)
                try {
                    val data = execute(event is LoomSignal.FromUser)
                    emit(LoomState.Loaded(data))
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    emit(LoomState.Error(e))
                }
            }
        }
        .combine(_modifier.onStart { emit { data: T -> data } }) { oldState, modifier ->
            mutex.withLock {
                if (oldState is LoomState.Loaded) {
                    val expectedData = modifier(oldState.data)
                    if (expectedData != oldState.data) {
                        LoomState.Loaded(expectedData)
                    } else {
                        oldState // Don't do anything if value is not changing
                    }
                } else {
                    oldState // Don't do anything if value is not changing
                }
            }
        }
        .catch { emit(LoomState.Error(it)) }
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(
                stopTimeoutMillis = 5_000,
                replayExpirationMillis = 0
            ),
            initialValue = LoomState.Init
        )

    override fun refresh(fromUser: Boolean) {
        _state.subscriptionCount.value > 0
        _state.tryEmit(if (fromUser) LoomSignal.FromUser else LoomSignal.Automatic)
    }

    override fun update(modifier: (T) -> T) {
        _modifier.tryEmit(modifier)
    }
}

```

#### Conclusion
The Loom metaphor emphasizes the importance of structured, efficient, and scalable state management. By weaving together streams of data and providing explicit controls for refreshing, Loom ensures that the UI remains responsive, consistent, and performant. This approach not only aligns with modern reactive programming paradigms but also provides a clear and elegant mechanism for managing dynamic states.

## 🚀 How to use

Cloning the repository into a local directory and checkout the desired branch:

```
git clone git@github.com:nphausg/loom.git
git checkout master
```

## 📝 Contributing
Contributing
We welcome contributions! If you're interested in contributing to LoomFlow, here are some ways you can help:
- Fix bugs 🐛
- Improve documentation 📚
- Enhance features 💡
- Add examples 🖥️
## ✨ How to Contribute
- Fork the repo.
- Clone your forked repo locally.
- Create a new branch for your fix or feature.
- Write tests for your changes (if applicable).
- Submit a pull request with a detailed explanation of your changes.
- Please feel free to contact me or make a pull request.

## 📄 License

LoomFlow is open-source and licensed under the MIT License.

## 🌍 Connect With me

Follow the repository for updates and improvements:
- GitHub: [@nphausg](https://github.com/nphausg/loomIn)
- Twitter: [@nphausg](https://x.com/nphausg)
- Medium: [@nphausg](https://medium.com/@nphausg)
- LinkedIn: [@nphausg](https://www.linkedin.com/in/nphausg)


<a href="https://revolut.me/nphausg" target="_blank"><img src="https://www.buymeacoffee.com/assets/img/custom_images/orange_img.png" alt="nphausg" style="height: 41px !important;width: 174px !important;box-shadow: 0px 3px 2px 0px rgba(190, 190, 190, 0.5) !important;-webkit-box-shadow: 0px 3px 2px 0px rgba(190, 190, 190, 0.5) !important;" ></a>

## 👥 Acknowledgements
Thanks to the Kotlin and Coroutine community for their support and contributions. We also want to thank all of our contributors who helped improve LoomFlow! 🙏

## ✨ Star, Fork, and Share! ✨

If you find LoomIn helpful, please ⭐️ star the repo and 🔁 fork it to use in your own projects. Your support helps others discover the project! Let’s make asynchronous programming easier for everyone! 😄🚀


## 👇 Author

<p>
    <a href="https://nphausg.medium.com/" target="_blank">
    <img src="https://avatars2.githubusercontent.com/u/13111806?s=400&u=f09b6160dbbe2b7eeae0aeb0ab4efac0caad57d7&v=4" width="96" height="96" alt="">
    </a>
</p>
