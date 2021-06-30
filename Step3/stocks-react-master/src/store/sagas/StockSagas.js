import { call, takeLatest, put, all } from 'redux-saga/effects';
import { appError } from '../actions/appActions';
import { loadStocks, symbolsChanged, symbolsLoaded, stocksLoaded } from '../actions/stockActions';
import StockService from '../../services/StockService';

function* fetchStockQuotes(action) {
  const stockService = new StockService();
  try {
    const stocks = yield call(stockService.getStocks.bind(stockService), action.symbols);
    yield put(stocksLoaded(stocks, action.symbols));
  }
  catch (e) {
    yield put(appError(e));
  }
}

function* fetchSymbols() {
  const stockService = new StockService();
  try {
    const symbols = stockService.getSavedSymbols();
    yield call(fetchStockQuotes, loadStocks(symbols));
  }
  catch (e) {
    yield put(appError(e));
  }
}

function* saveSymbols(action) {
  const stockService = new StockService();
  try {
    stockService.saveSymbols(action.symbols);
  }
  catch (e) {
    yield put(appError(e));
  }
}

function* fetchAllSymbols() {
  const stockService = new StockService();
  try {
    const allSymbols = yield call(stockService.getAllSymbols.bind(stockService));
    yield put(symbolsLoaded(allSymbols));
  }
  catch (e) {
    yield put(appError(e));
  }
}

function* addSymbol(action) {
  const symbol = action.symbol;
  if(!symbol) return;
  const stockService = new StockService();
  const symbols = stockService.getSavedSymbols().split(',');
  if (symbols.indexOf(symbol) < 0) {
    symbols.push(symbol);
    yield put(symbolsChanged(null, symbols.join(','), 'ADD'));
    yield put(loadStocks(symbols));
  }

}

function* rootSaga() {
  yield all([
    takeLatest('LOAD_STOCKS', fetchStockQuotes),
    takeLatest('DASHBOARD_LOADED', fetchSymbols),
    takeLatest('SYMBOLS_CHANGED', saveSymbols),
    takeLatest('ADD_STOCK', fetchAllSymbols),
    takeLatest('STOCK_ADDED', addSymbol)
  ]);
}

export default rootSaga;
