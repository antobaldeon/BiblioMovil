import SwiftUI
import Shared

@main
struct iOSApp: App {
    init() {
        KoinInitializerKt.initKoinIos()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
