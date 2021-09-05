import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Button, Icon, Modal } from 'semantic-ui-react';

import './StockDashboardLineItem.scss';

class StockDashboardLineItem extends Component {

    constructor() {
        super();
        this.state = {};
    }

    getColor(stock) {
        return stock.change < 0 ? 'red' : 'green';
    }

    getArrow(stock) {
        return stock.change < 0 ? 'arrow down' : 'arrow up';
    }

    render() {
        const { stock, displayIndex, displayFields, displayChanged, symbolModalOpen } = this.props;
        return (
            <div>
                <li className='stock-line-item'>
                    <span className='symbol'>
                        <Icon name={this.getArrow(stock)} color={this.getColor(stock)}></Icon>
                        {stock.symbol}
                    </span>
                    <span className='price'>
                        ${stock.latestPrice}
                        <span className='diff'>
                            {stock.isRemoved}
                            <Button
                                // onClick={displayChanged}
                                size='mini'
                                basic
                                color={this.getColor(stock)}
                                content={stock[displayFields[displayIndex]]} />
                                <Icon className='toggle-remove'
                                    name={'minus circle'}
                                    color={'red'}
                                    // onClick={this.toggleRemove.bind(this)}
                                    >
                                    </Icon>
                            {
                                this.props.isEditMode &&
                                <Icon className='toggle-remove'
                                    name={this.state.isRemoved ? 'plus circle' : 'minus circle'}
                                    color={this.state.isRemoved ? 'green' : 'red'}
                                    onClick={this.toggleRemove.bind(this)}></Icon>
                            }
                        </span>
                    </span>
                </li>
            </div>
        );
    }

    toggleRemove() {
        this.props.stock.isRemoved = !this.props.stock.isRemoved;
        this.setState(state => state.isRemoved = !state.isRemoved);
    }
}

StockDashboardLineItem.propTypes = {
    isEditMode: PropTypes.bool.isRequired,
    stock: PropTypes.object.isRequired,

    displayIndex: PropTypes.number.isRequired,
    displayFields: PropTypes.arrayOf(PropTypes.string).isRequired,
    displayChanged: PropTypes.func.isRequired
};

export default StockDashboardLineItem;