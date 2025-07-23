export default function TypingIndicator() {
    return (
        <div className="flex justify-start">
            <div className="px-4 py-3 rounded-2xl bg-gray-100 dark:bg-gray-800 rounded-bl-none">
                <div className="flex space-x-1">
                    <div className="w-2 h-2 rounded-full bg-gray-400 animate-bounce"></div>
                    <div className="w-2 h-2 rounded-full bg-gray-400 animate-bounce delay-150"></div>
                    <div className="w-2 h-2 rounded-full bg-gray-400 animate-bounce delay-300"></div>
                </div>
            </div>
        </div>
    );
}