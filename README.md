# Github User Explorer
## Development principles, patterns & practices

The app follows Clean Architecture concept of Uncle Bob, dividing into 3 layers `presentation`(components), `data` and `domain`.
- **Domain** layer: executes business logics and is totally independent to other layers hence only pure Kotlin packages. Including UseCases, Entities and Repository Interfaces(Dependcy Inversion principle)
- **Data** layer: dispenses data to domain layer by implementing interfaces of domain. It only has knowledge about **Domain** layer.
- **Presentation** layer: a whole MVVM executes UI logic and is Android specific.

Caching Mechanism is implemented which decorates ApiRepository with CachingRepository. If there's no cache available, the request is delegated to ApiRepository.

The app also follows SOLID principles that makes unit testing easy and doable with high coverage and sure with high maintainability.

## Code folder structure and key Java/Kotlin libraries
Top folders level are divided to architecture layers and their usages:
- **Components**: Presentation layer
- **Data**: Data Layer
- **Domain**: Domain Layer
- **Common**: includes common helpers
- **Di**: Components and Modules for setting up Dagger for DI

Frameworks and Libraries:
- **Dagger**: Dependency Injection
- **Retrofit**: HTTP Client
- **RxJava/Android**: Reactive Extension for handling asynchronous and event-based programs.
- **Room**: Library for database on Android, which provides an abstraction over SQLite.
- **Mockito**: Mocking framework

## Testing

Significant amount of unit tests together with UI tests which covers all business logics and appearance of UI.

## Techincal Note

Currently app is using Github REST API which requires a search request and individual request for each user detail (which is costly and not responsive to open each user detail screen) because the response of search request does not have enough data we need to display a user detail. 

For improvement (upgrade), we can use Github Graphql API. This way, right in the search query to Github Graphql, we can define and get all user detail fields in the search response and save to local database for later fetching.
