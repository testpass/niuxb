import React from 'react';
import { hot } from 'react-hot-loader';


class App extends React.Component {
  state = {
    index: 1,
  };

  handleClick = () => {
    const { index } = this.state;
    this.setState({
      index: index + 1,
    });
  };


  render() {
    return (
      <div>
        123
      </div>
    );
  }
}

export default hot(module)(App);
