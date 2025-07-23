
import ChatWindow from './components/ChatWindow';

function App() {
    return (
        <div className="min-h-screen bg-gray-50 dark:bg-gray-900 text-gray-900 dark:text-gray-100">
            <header className="border-b border-gray-200 dark:border-gray-800 p-4">
                <h1 className="text-2xl font-bold">AI Assistant</h1>
            </header>
            <main>
                <ChatWindow />
            </main>
        </div>
    );
}

export default App
