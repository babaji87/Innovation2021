import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { fromEvent } from 'rxjs';
import { debounceTime } from 'rxjs/operators';
import { connect } from 'react-redux';
import { Button, Modal, Input, Icon, List } from 'semantic-ui-react';
import { searchStocks, selectStock } from '../../store/actions/addStockActions';
import './AddStock.scss';

class AddStock extends Component {

  render() {
    const { onClose, open, close, matchingStocks, selectedStock } = this.props;
    return (
      <Modal open={open}
        onClose={onClose}>
        <Modal.Header>Add Stock To Dashboard</Modal.Header>
        <Modal.Content>
          <Input icon placeholder='Enter a symbol'>
            <input ref={ref => this.handleInputRef(ref)} />
            <Icon name='search' />
          </Input>
          {matchingStocks && matchingStocks.length > 0 &&
            <List className="matching-stocks" selection verticalAlign='middle'>
              {matchingStocks.map(s => {
                return <List.Item key={s.symbol} onClick={() => this.onSelect(s)} className={this.getClassName(s.symbol)}>
                  <List.Content>
                    <List.Header>{s.symbol} - {s.companyName}</List.Header>
                  </List.Content>
                </List.Item>
              })}
            </List>
          }
        </Modal.Content>
        <Modal.Actions>
          <Button onClick={onClose.bind(this)}
            content='Close' />
          <Button
            disabled={!selectedStock}
            onClick={() => this.onSave(selectedStock.symbol)}
            positive
            content='Save' />
        </Modal.Actions>
      </Modal>
    );
  }

  onSave(symbol) {
    this.props.onSave(symbol);
  }

  onSelect(selected) {
    this.props.selectStock(selected);
  }

  getClassName(symbol){
    if(this.props.selectedStock && this.props.selectedStock.symbol === symbol){
      return "stock-selected";
    }else{
      return "";
    }
  }

  handleInputRef(input) {
    if (input) {
      const symbolInput$ = fromEvent(input, 'keyup');
      this.inputSubs = symbolInput$.pipe(debounceTime(1000))
        .subscribe(s => this.findSymbol(s.target.value));
    }
    else {
      this.inputSubs.unsubscribe();
    }
  }

  findSymbol(input) {
    this.props.searchStocks(input, this.props.allSymbols);
  }

}

AddStock.propTypes = {
  allSymbols: PropTypes.array.isRequired,
  open: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
  onSave: PropTypes.func.isRequired
};

const mapStateToProps = state => {
  return {...state.addStocks};
};

const mapDispatchToProps = dispatch => ({
  searchStocks: (input, allSymbols) => dispatch(searchStocks(input, allSymbols)),
  selectStock: stock => dispatch(selectStock(stock))
});

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(AddStock);
