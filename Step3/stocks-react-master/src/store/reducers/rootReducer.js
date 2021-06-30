import { combineReducers } from 'redux';
import stockReducers from './stockReducers';
import addStockReducers from './addStockReducers';

const defaultState = {
  error: null
}
const appReducer = (state = defaultState, action) => {
  switch (action.type) {
    case 'APP_ERROR': {
      return Object.assign({...defaultState}, {
        error: action.error
      });
    }
    default: {
      return defaultState;
    }
  }
}

export default combineReducers({
  dashboard: stockReducers,
  addStocks: addStockReducers,
  app: appReducer
})
