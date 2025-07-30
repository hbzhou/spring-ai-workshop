import {useState, useCallback, useRef} from 'react';
import axios, {type AxiosRequestConfig, type CancelTokenSource } from 'axios';

export default function useStreamingResponse() {
    const [isStreaming, setIsStreaming] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const cancelSource = useRef<CancelTokenSource | null>(null);

    const streamResponse = useCallback(async (
        prompt: string,
        onChunk: (chunk: string) => void,
        onComplete: () => void
    ) => {
        setIsStreaming(true);
        setError(null);

        try {
            // Create cancel token for aborting requests
            cancelSource.current = axios.CancelToken.source();

            const config: AxiosRequestConfig = {
                responseType: 'stream',
                cancelToken: cancelSource.current.token,
                onDownloadProgress: (progressEvent) => {
                    const chunk = progressEvent.event.target.responseText;
                    if (chunk) {
                        onChunk(chunk);
                    }
                }
            };
            await axios.get(`/api/chatbot/stream?prompt=${prompt}`, config);
            onComplete();
        } catch (err) {
            if (!axios.isCancel(err)) {
                setError('Failed to stream response');
                console.error('Streaming error:', err);
            }
        } finally {
            setIsStreaming(false);
            cancelSource.current = null;
        }
    }, []);

    const cancelStream = useCallback(() => {
        if (cancelSource.current) {
            cancelSource.current.cancel('User cancelled the request');
        }
    }, []);

    return { streamResponse, cancelStream, isStreaming, error };
}