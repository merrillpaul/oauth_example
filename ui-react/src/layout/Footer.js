import React from 'react';
import {Grid, Row, Col} from 'react-bootstrap';

export default class Footer extends React.Component {
    render() {
        return (
            <footer>
                <Grid>
                    <Row>
                        <Col lg={12} xs={12} md={3}>
                            <div>
                                <h3>Footer Info</h3>
                            </div>
                        </Col>
                    </Row>
                </Grid>
            </footer>
        );
    }
}