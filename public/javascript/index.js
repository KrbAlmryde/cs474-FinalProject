/**
 * Created by krbalmryde on 12/11/16.
 */

function HandleStream(chart) {
    return (event) => {
        console.log(event);
        let result = JSON.parse(event.data);
        let string = result.sentiment.emotion
                + ":" + result.sentiment.score
                + "\n\t" + result.tweet.author
                + "\n\t\t" + result.tweet.body
            ;
        console.log(string);
        // This is where d3 comes in...
        d3.select("#tweet")
            .append('p')
            .html(string)


        chart.datum(DataConverter(result))
    }
}

const colorFunc = d3.scale.linear().range(['red', 'orange', 'grey', 'blue', 'green']).domain([-5,5]);
const sizeFunc = d3.scale.linear().range([-6, 6]).domain([-5,5]);

function DataConverter(result) {
    return {
        category: result.sentiment.emotion,
        time: new Date(),
        type:"circle",
        opacity: 0.8,
        size: Math.abs(sizeFunc(result.sentiment.score)),
        color: colorFunc(result.sentiment.score)
    }
}

function SenitmentGraph() {
    let limit = 60 * 1,
        duration = 750,
        now = new Date(Date.now() - duration);
    let data = [
        {
            category: "VERY_NEGATIVE",
            time: new Date().getTime(),
            type:"circle",
            opacity: 0.8,
            size: 5,
            color: 'red'

        },
        {
            category: "NEGATIVE",
            time: new Date().getTime(),
            type:"circle",
            opacity: 0.8,
            size: 5,
            color: 'orange'
        },
        {
            category: "NEUTRAL",
            time: new Date().getTime(),
            type:"circle",
            opacity: 0.8,
            size: 5,
            color: 'grey'
        },
        {
            category: "POSITIVE",
            time: new Date().getTime(),
            type:"circle",
            opacity: 0.8,
            size: 5,
            color: 'blue'
        },
        {
            category: "VERY_POSITIVE",
            time: new Date().getTime(),
            type:"circle",
            opacity: 0.8,
            size: 5,
            color: 'green'
        }
    ]

    var margin = {top: 20, right: 20, bottom: 30, left: 30},
        width = 960 - margin.left - margin.right,
        height = 500 - margin.top - margin.bottom;

    let chart = realTimeChartMulti()
        .title("Sentimental Twitter")
        .yTitle("Sentiment")
        .xTitle("Time")
        .yDomain(["VERY_POSITIVE", "POSITIVE", "NEUTRAL", "NEGATIVE", "VERY_NEGATIVE"].reverse())
        .border(true)
        .width(width)
        .height(height);
        // .data(data)

    let chartDiv = d3.select("#vizGraph").append('div')
        .attr('id', 'chartDiv')
        .call(chart)

    const tX = 5; // time constant, multiple of one second
    const meanMs = 1000 * tX, // milliseconds
             dev = 200 * tX; // std dev

// define time scale
    const timeScale = d3.scale.linear()
        .domain([300 * tX, 1700 * tX])
        .range([300 * tX, 1700 * tX])
        .clamp(true);

    return chart
}



function SenitmentGraph2() {
    let groups = {
        VERY_NEGATIVE: {
            value: 0,
            color: 'red',
            data: d3.range(limit).map(function() {
                return 0
            })
        },
        NEGATIVE: {
            value: 0,
            color: 'orange',
            data: d3.range(limit).map(function() {
                return 0
            })
        },
        NEUTRAL: {
            value: 0,
            color: 'grey',
            data: d3.range(limit).map(function() {
                return 0
            })
        },

        POSITIVE: {
            value: 0,
            color: 'blue',
            data: d3.range(limit).map(function() {
                return 0
            })
        },
        VERY_POSITIVE: {
            value: 0,
            color: 'green',
            data: d3.range(limit).map(function() {
                return 0
            })
        }
    };


    const x = d3.scaleTime()
        .domain([now - (limit - 2), now - duration])
        .range([0, width]);

    const y = d3.scaleLinear()
        .domain([-5, 5])
        .range([height, 0]);

    const line = d3.line()
        .curve(d3.curveBasis)
        .x((d, i) => x(now - (limit - 1 - i) * duration) )
        .y((d) => y(d) );

    let svg = d3.select('#vizGraph').append('svg')
        .attr('class', 'chart')
        .attr("width", width + margin.left + margin.right)
        .attr("height", height + margin.top + margin.bottom)
        .append('g')
        .attr("transform", "translate(" + margin.left + "," + margin.top + ")");


    let axis = svg.append('g')
        .attr("class", "axis axis--x")
        .attr("transform", "translate(0," + y(0) + ")")
        .call(d3.axisBottom(x))

    let paths = svg.append('g');

    for (let name in groups) {
        let group = groups[name];
        group.path = paths.append('path')
            .data([group.data])
            .attr('class', name + ' group')
            .style('stroke', group.color)
    }



    function tick(data) {
        now = new Date();
        console.log("tick:", data)
        // Add new values
        group = groups[data.sentiment.emotion];
        group.data.push(data.sentiment.score)
        group.path.attr('d', line)

        // Shift domain
        x.domain([now - (limit - 2) * duration, now - duration]);

        // Slide x-axis left

        axis.transition()
            .duration(duration)
            .ease(d3.easeLinear)
            .call(d3.axisBottom(x))//.call(x.axis);

        // Slide paths left
        group.path//.attr('transform', null)
            .transition()
            .duration(duration)
            .ease(d3.easeLinear)
            .attr('transform', 'translate(' + x(now - (limit - 1) * duration) + ')')

        // Remove oldest data point from each group
        for (let name in groups) {
            let group = groups[name];
            group.data.shift()
        }
    }

    return tick;

}