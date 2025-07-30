/** @type {import('tailwindcss').Config} */
module.exports = {
    content: [
        "./index.html",
        "./src/**/*.{js,ts,jsx,tsx}",
    ],
    theme: {
        extend: {
            colors: {
                'ai-dark': '#0f172a',
                'ai-light': '#f8fafc',
            }
        },
    },
    plugins: [],
}