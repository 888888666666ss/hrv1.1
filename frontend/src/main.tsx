import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import './index.css';
import BeisenHRApp from './BeisenHRApp.tsx';

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <BeisenHRApp />
  </StrictMode>,
);
