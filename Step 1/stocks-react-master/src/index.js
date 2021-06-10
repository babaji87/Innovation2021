import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import { Provider } from 'react-redux';
import { createStore, applyMiddleware, compose } from 'redux';
import rootreducer from './store/reducers/rootReducer';
import registerServiceWorker from './registerServiceWorker';
import createSagaMiddleware from 'redux-saga';
import rootSaga from './store/sagas/StockSagas';

import './index.scss';
import 'semantic-ui-css/semantic.min.css';
const composeEnhancer = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;
const sagaMiddleware = createSagaMiddleware();
const store = createStore(
  rootreducer,
  composeEnhancer(applyMiddleware(sagaMiddleware))
);


sagaMiddleware.run(rootSaga);

ReactDOM.render(
  <Provider store={store}>
    <App />
  </Provider>,
  document.getElementById('root')
);

registerServiceWorker();
