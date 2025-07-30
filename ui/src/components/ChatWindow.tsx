import {useState, useRef, useEffect} from 'react';
import MessageBubble from './MessageBubble';
import InputArea from './InputArea';
import TypingIndicator from './TypingIndicator';
import useStreamingResponse from '../hooks/useStreamingResponse';

export type Message = {
    id: string;
    content: string;
    role: 'user' | 'assistant';
};

const initMessages: Message[] = [{
    id: '1',
    content: 'Hello! How can I assist you today?',
    role: 'assistant'
}];

export default function ChatWindow() {
    const [messages, setMessages] = useState<Message[]>(initMessages);
    const messagesEndRef = useRef<HTMLDivElement>(null);
    const {streamResponse, cancelStream, isStreaming} = useStreamingResponse();

    const handleSend = async (input: string) => {
        if (!input.trim() || isStreaming) return;

        // Add user message
        const userMessage: Message = {
            id: Date.now().toString(),
            content: input,
            role: 'user'
        };

        setMessages(prev => [...prev, userMessage]);

        // Add empty assistant message
        const assistantMessage: Message = {
            id: `temp-${Date.now()}`,
            content: '',
            role: 'assistant'
        };

        setMessages(prev => [...prev, assistantMessage]);

        let content: string = '';
        try {
            await streamResponse(
                input,
                (chunk) => {setMessages(prev => prev.map(msg => msg.id === assistantMessage.id ? {...msg, content: chunk} : msg));},
                () => {}
            );
        } catch (error) {
            setMessages(prev => prev.map(msg => msg.id === assistantMessage.id ? {...msg, content: content + '\n\n[Response interrupted]'} : msg));
        }
    };

    const handleCancel = () => {
        if (isStreaming) {
            cancelStream();
        }
    };

    // Auto-scroll to bottom
    useEffect(() => {
        messagesEndRef.current?.scrollIntoView({behavior: 'smooth'});
    }, [messages]);

    return (
        <div className="flex flex-col h-screen max-w-4xl mx-auto">
            <div className="flex-1 overflow-y-auto p-4 space-y-6">
                {messages.map((message) => (
                    <MessageBubble key={message.id} message={message}/>
                ))}
                {isStreaming && <TypingIndicator/>}
                <div ref={messagesEndRef}/>
            </div>
            <InputArea
                onSend={handleSend}
                onCancel={handleCancel}
                isStreaming={isStreaming}
            />
        </div>
    );
}