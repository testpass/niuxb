import React from 'react';
import {
  render,
} from 'react-dom';
import App from './App';
import Router from './configs/router.config'

render(<Router />, 
  document.getElementById('root'));
