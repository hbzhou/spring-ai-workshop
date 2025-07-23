import { useState, useRef, type KeyboardEvent } from 'react';

type InputAreaProps = {
    onSend: (content: string) => void;
    onCancel: () => void;
    isStreaming: boolean;
};

export default function InputArea({ onSend, onCancel, isStreaming }: InputAreaProps) {
    const [input, setInput] = useState('');
    const textareaRef = useRef<HTMLTextAreaElement>(null);

    const handleSubmit = () => {
        if (!input.trim()) return;
        onSend(input);
        setInput('');
    };

    const handleKeyDown = (e: KeyboardEvent<HTMLTextAreaElement>) => {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            handleSubmit();
        }
    };

    return (
        <div className="p-4 border-t border-gray-200 dark:border-gray-700">
            <div className="flex items-end rounded-lg border border-gray-300 dark:border-gray-600 bg-white dark:bg-gray-900">
        <textarea
            ref={textareaRef}
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyDown={handleKeyDown}
            placeholder="Ask anything..."
            className="flex-1 px-4 py-3 bg-transparent resize-none outline-none max-h-40"
            rows={1}
            disabled={isStreaming}
        />
                {isStreaming ? (
                    <button
                        onClick={onCancel}
                        className="m-2 px-4 py-2 bg-red-600 text-white rounded-lg"
                    >
                        Stop
                    </button>
                ) : (
                    <button
                        onClick={handleSubmit}
                        disabled={!input.trim()}
                        className="m-2 px-4 py-2 bg-blue-600 text-white rounded-lg disabled:opacity-50"
                    >
                        Send
                    </button>
                )}
            </div>
        </div>
    );
}