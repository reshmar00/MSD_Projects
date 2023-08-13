
let schoolsInStateDic;
let jsonData;
let tooltip;

let statesByBodyPart = {};
window.onload = async function () {
    jsonData = await d3.json("body_by_school.json"); // loading the data in 
    myData = createDataForBarChart(jsonData);


    schoolsInStateDic = dictSchoolsInState(jsonData)
    let uniqueBodyParts = getbannedBodyPart(); // find all body parts
    statesByBodyPart = getStatesForBodyPartDictionary(jsonData, uniqueBodyParts)
    //let bodyPartsBySchools = getschoolsbyBodyParts(jsonData, allBannedBodyParts)
    //let bodyPartsByStates = getStatesByBodyParts(jsonData)
    const allstates = getAllStates(jsonData)
    let data = createDataForBarChart(jsonData);
    console.log("data ", data)
    createAstackedGraph(data, allstates, uniqueBodyParts)
};


// grouping body parts by state
function getStatesForBodyPartDictionary(jsonData, bannedBodyParts) {
    let bodyPartsByState = {};
    for (let bodyPart of bannedBodyParts) {
        const bannedState = [];
        for (const obj of jsonData) {
            if (obj.state != null && obj.bodyParts.includes(bodyPart)  && !bannedState.includes(obj.state)) {
                bannedState.push(obj.state);
            }
        }

        bodyPartsByState[bodyPart] = bannedState;
    }
    return bodyPartsByState;
}

/**
 * f(x) that retuns an array of all unique body parts
 * @param {*} jsonData jsonObject
 * @returns an array of all unique body parts
 */
function getbannedBodyPart() {
    let bodyParts = ['midriff', 'cleavage', 'back', 'chest', 'buttocks', 'torso'] // this is how the body parts are written in the json data. Order  matters here. Hard corded the order based on what body was mostly banned in schools
    return Array.from(bodyParts);
}

// function to group schools by the banned body party 
function getschoolsbyBodyParts(jsonData, bannedBodyParts) {
    // object  to store all body parts as key  and all the names of highschools that have banned it 
    let bannedSchoolsByBodyPart = {};
    for (const bodyPart of bannedBodyParts) {
        const bannedSchools = [];
        for (const item of jsonData) {
            // Check if the current school has the current body part banned
            if (Array.isArray(item.bodyParts) && item.bodyParts.includes(bodyPart)) {
                // If yes, add the school to the array of banned schools
                bannedSchools.push(item.schoolName);
            }
        }
        bannedSchoolsByBodyPart[bodyPart] = bannedSchools; // key / value pair 
    }
    return bannedSchoolsByBodyPart;
}

// grouping body parts by state
function getStatesByBodyParts(jsonData) {
    const bodyPartsByState = [];
    for (const entry of jsonData) {
        const { BODYPART, STATE, SCHOOLCOUNT } = entry;
        if (entry.bodyParts != null) {
            entry.bodyParts.forEach(bodyPart => {
                if (bodyPart != null) {
                    const existingData = bodyPartsByState.find(data => data.BODYPART === bodyPart && data.STATE === entry.state);
                    if (existingData) {
                        // If the combination of bodyPart and state exists, increment the schoolCount for that entry
                        existingData.SCHOOLCOUNT = 1 + existingData.SCHOOLCOUNT;
                    } else {
                        // If the combination of bodyPart and state does not exist, add a new entry to bodyPartsByState
                        bodyPartsByState.push({ BODYPART: bodyPart, STATE: entry.state, SCHOOLCOUNT: 1 });
                    }
                }
            });
        }
    }

    return bodyPartsByState;
}


// Add mouseover and mouseout event listeners using D3
// Polygon class names
const svgPathCleavage = '.cleavage';
const svgPolygonChest = '.chest';
const svgRectTorso = '.torso';
const svgPolygonBack = '.back';
const svgPolygonMidriff = '.midriff';
const svgPolygonButtocks = '.buttocks';

// Get the references to the text boxes by their classes
const labelPathCleavage = d3.select('.cleavage-label');
const labelPolygonChest = d3.select('.chest-label');
const labelRectTorso = d3.select('.torso-label');
const labelPolygonBack = d3.select('.back-label');
const labelPolygonMidriff = d3.select('.midriff-label');
const labelPolygonButtocks = d3.select('.buttocks-label');

// Define a variable to keep track of the currently clicked body part and label
let activeBodyPart = null;

//<div id="body-parts-list">

// create new tooltip
let newTooltip = d3.select("#body-parts-list")
    .append("div")
    .attr("id", "chart")
    .attr("class", "newTooltip");

// Define the mouseover and mouseout handlers for each polygon

function getStatesForBodyPart(bodyPart){
    let returnValue = [];
    const bodyParts = Object.keys(statesByBodyPart);
    for (let bp of bodyParts) {
        if (bp === bodyPart) {
            returnValue = statesByBodyPart[bp];
            console.log('Key:', bp, 'Value:', statesByBodyPart[bp]);
        }
    }

    let newLine = "";
    for (const ret of returnValue) {
        newLine += ret + "<br>";
    }

    return newLine;
}


// Attach the handlers to the corresponding polygons, rects, and paths
// Cleavage
d3.select(svgPathCleavage)
    .attr('data-clicked', 'false')
    .on('mouseover', () => {
        const isClicked = d3.select(svgPathCleavage).attr('data-clicked') === 'true';
        if (!isClicked) {
            labelPathCleavage.classed('highlighted', true);
            d3.select(svgPathCleavage).style('fill', '#FF0000');
        }
        // activate tooltip
        newTooltip.style("opacity", 1).html("Banned in <b>106</b> schools and" + "<br>" + "in the following states:" + "<br>" + getStatesForBodyPart('cleavage'));
    })
    .on('mouseout', () => {
        const isClicked = d3.select(svgPathCleavage).attr('data-clicked') === 'true';
        if (!isClicked) {
            labelPathCleavage.classed('highlighted', false);
            d3.select(svgPathCleavage).style('fill', '#FCC992');
        }
        // deactivate tooltip
        newTooltip.style("opacity", 1);
    })
    .on('click', (event) => {
        // console.log("Entered cleavage");
        if (activeBodyPart === svgPathCleavage) {
            // Clicked on the same part again, deactivate everything
            // console.log("Entering if");
            deactivateAllBodyParts();
        } else {
            console.log("Entering else");
            // Deactivate the current part (if any)...
            deactivateActiveBodyPart();
            // console.log("Deactivated Body part");
            // ... and activate the new part
            activateBodyPart(svgPathCleavage, labelPathCleavage);

        }
    });

// Chest
d3.select(svgPolygonChest)
    .attr('data-clicked', 'false')
    .on('mouseover', () => {
        const isClicked = d3.select(svgPolygonChest).attr('data-clicked') === 'true';
        if (!isClicked) {
            labelPolygonChest.classed('highlighted', true);
            d3.select(svgPolygonChest).style('fill', '#FF0000');
        }
        // activate tooltip
        newTooltip.style("opacity", 1).html("Banned in <b>69</b> schools and" + "<br>" + "in the following states:" + "<br>" + getStatesForBodyPart('chest'));

    })
    .on('mouseout', () => {
        const isClicked = d3.select(svgPolygonChest).attr('data-clicked') === 'true';
        if (!isClicked) {
            labelPolygonChest.classed('highlighted', false);
            d3.select(svgPolygonChest).style('fill', '#FCC992');
        }
        // deactivate tooltip
        newTooltip.style("opacity", 1);
    })
    .on('click', () => {
        if (activeBodyPart === svgPolygonChest) {
            // Clicked on the same part again, deactivate everything
            deactivateAllBodyParts();
        } else {
            // Deactivate the current part (if any) and activate the new part
            deactivateActiveBodyPart();
            activateBodyPart(svgPolygonChest, labelPolygonChest);
            // Highlight corresponding bars and activate the new part
            highlightBars('chest');
        }
    });

// Torso
d3.select(svgRectTorso)
    .attr('data-clicked', 'false')
    .on('mouseover', () => {
        const isClicked = d3.select(svgRectTorso).attr('data-clicked') === 'true';
        if (!isClicked) {
            labelRectTorso.classed('highlighted', true);
            d3.select(svgRectTorso).style('fill', '#FF0000');
        }
        // activate tooltip
        newTooltip.style("opacity", 1).html("Banned in <b>33</b> schools and" + "<br>" + "in the following states:" + "<br>" + getStatesForBodyPart('torso'));
    })
    .on('mouseout', () => {
        const isClicked = d3.select(svgRectTorso).attr('data-clicked') === 'true';
        if (!isClicked) {
            labelRectTorso.classed('highlighted', false);
            d3.select(svgRectTorso).style('fill', '#FCC992');
        }
        // deactivate tooltip
        newTooltip.style("opacity", 1);
    })
    .on('click', () => {
        if (activeBodyPart === svgRectTorso) {
            // Clicked on the same part again, deactivate everything
            deactivateAllBodyParts();
        } else {
            // Deactivate the current part (if any) and activate the new part
            deactivateActiveBodyPart();
            activateBodyPart(svgRectTorso, labelRectTorso);
            // Highlight corresponding bars and activate the new part
            highlightBars('torso');
        }
    });

// Back
d3.select(svgPolygonBack)
    .attr('data-clicked', 'false')
    .on('mouseover', () => {
        const isClicked = d3.select(svgPolygonBack).attr('data-clicked') === 'true';
        if (!isClicked) {
            labelPolygonBack.classed('highlighted', true);
            d3.select(svgPolygonBack).style('fill', '#FF0000');
        }
        // activate tooltip
        newTooltip.style("opacity", 1).html("Banned in <b>74</b> schools and" + "<br>" + "in the following states:" + "<br>" + getStatesForBodyPart('cleavage'));
    })
    .on('mouseout', () => {
        const isClicked = d3.select(svgPolygonBack).attr('data-clicked') === 'true';
        if (!isClicked) {
            labelPolygonBack.classed('highlighted', false);
            d3.select(svgPolygonBack).style('fill', '#FCC992');
        }
        // deactivate tooltip
        newTooltip.style("opacity", 1);
    })
    .on('click', () => {
        if (activeBodyPart === svgPolygonBack) {
            // Clicked on the same part again, deactivate everything
            deactivateAllBodyParts();
        } else {
            // Deactivate the current part (if any) and activate the new part
            deactivateActiveBodyPart();
            activateBodyPart(svgPolygonBack, labelPolygonBack);
            // Highlight corresponding bars and activate the new part
            highlightBars('back');
        }
    });

// Midriff
d3.select(svgPolygonMidriff)
    .attr('data-clicked', 'false')
    .on('mouseover', () => {
        const isClicked = d3.select(svgPolygonMidriff).attr('data-clicked') === 'true';
        if (!isClicked) {
            labelPolygonMidriff.classed('highlighted', true);
            d3.select(svgPolygonMidriff).style('fill', '#FF0000');
        }
        // activate tooltip
        newTooltip.style("opacity", 1).html("Banned in <b>340</b> schools and" + "<br>" + "in the following states:" + "<br>" + getStatesForBodyPart('cleavage'));
    })
    .on('mouseout', () => {
        const isClicked = d3.select(svgPolygonMidriff).attr('data-clicked') === 'true';
        if (!isClicked) {
            labelPolygonMidriff.classed('highlighted', false);
            d3.select(svgPolygonMidriff).style('fill', '#FCC992');
        }
        // deactivate tooltip
        newTooltip.style("opacity", 1);
    })
    .on('click', () => {
        if (activeBodyPart === svgPolygonMidriff) {
            // Clicked on the same part again, deactivate everything
            deactivateAllBodyParts();
        } else {
            // Deactivate the current part (if any) and activate the new part
            deactivateActiveBodyPart();
            activateBodyPart(svgPolygonMidriff, labelPolygonMidriff);
            // Highlight corresponding bars and activate the new part
            highlightBars('midriff');
        }
    });

// Buttocks
d3.select(svgPolygonButtocks)
    .attr('data-clicked', 'false')
    .on('mouseover', () => {
        const isClicked = d3.select(svgPolygonButtocks).attr('data-clicked') === 'true';
        if (!isClicked) {
            labelPolygonButtocks.classed('highlighted', true);
            d3.select(svgPolygonButtocks).style('fill', '#FF0000');
        }
        // activate tooltip
        newTooltip.style("opacity", 1).html("Banned in <b>54</b> schools and" + "<br>" + "in the following states:" + "<br>" + getStatesForBodyPart('cleavage'));
    })
    .on('mouseout', () => {
        const isClicked = d3.select(svgPolygonButtocks).attr('data-clicked') === 'true';
        if (!isClicked) {
            labelPolygonButtocks.classed('highlighted', false);
            d3.select(svgPolygonButtocks).style('fill', '#FCC992');
        }
        // deactivate tooltip
        newTooltip.style("opacity", 1);
    })
    .on('click', () => {
        if (activeBodyPart === svgPolygonButtocks) {
            // Clicked on the same part again, deactivate everything
            deactivateAllBodyParts();
        } else {
            // Deactivate the current part (if any) and activate the new part
            deactivateActiveBodyPart();
            activateBodyPart(svgPolygonButtocks, labelPolygonButtocks);
            // Highlight corresponding bars and activate the new part
            highlightBars('buttocks');
        }
    });

// Function to deactivate the currently clicked body part
function deactivateActiveBodyPart() {
    if (activeBodyPart) {
        const isClicked = d3.select(activeBodyPart).attr('data-clicked') === 'true';
        d3.select(activeBodyPart).attr('data-clicked', 'false');
        d3.select(activeBodyPart).style('fill', isClicked ? '#FCC992' : '#FF0000');
        if (activeBodyPart !== svgPathCleavage) {
            d3.select(activeBodyPart + '-label').classed('highlighted', false);
        }
        activeBodyPart = null;
    }
}

// Function to activate a body part and its label
function activateBodyPart(bodyPartSelector, labelSelector) {
    activeBodyPart = bodyPartSelector;
    const isClicked = d3.select(activeBodyPart).attr('data-clicked') === 'true';
    d3.select(activeBodyPart).attr('data-clicked', 'true');
    d3.select(activeBodyPart).style('fill', isClicked ? '#FCC992' : '#FF0000');
    d3.select(labelSelector).classed('highlighted', true);
}


// Function to deactivate all body parts
function deactivateAllBodyParts() {
    deactivateActiveBodyPart();
    d3.selectAll('.body-part')
        .attr('data-clicked', 'false')
        .style('fill', '#FCC992');
    d3.selectAll('.label')
        .classed('highlighted', false);
}

var mouseleave = function (event, d) {
    if (!stateIsClicked) {
        d3.selectAll(".myRect").style("opacity", 0.8);
        Object.keys(clickedStates).forEach(bodyPart => {
            if (!clickedStates[bodyPart]) {
                d3.select('.' + bodyPart).style('fill', '#FCC992');
                labelElement.classed('highlighted', false);
            }
        });
    }
    if (!stateIsClicked) {
        tooltip.style("opacity", 0.5);
        smalltooltip.style("opacity", 0).text("");
    }
};


// function to get all states
function getAllStates(data) {
    let States = new Set();
    for (const object of data) {
        if (object.state != null) {
            States.add(object.state);
        }
    }
    return Array.from(States);
}

function countAllSchools(data) {
    let schools = new Set();
    for (const object of data) {
        if (object.schoolName != null) {
            schools.add(object.schoolName);
        }
    }
    // console.log("Total of schools is ", schools.size)
    return schools.size;
}

function getSchoolsInAGivenState(state, bodyPart) {
    let listOfSchools = schoolsInStateDic[state];
    let bannedSchools = [];

    for (let i = 0; i < listOfSchools.length; i++) {
        let schoolname = listOfSchools[i];
        let schoolData = jsonData.find(item => item.schoolName === schoolname);

        // console.log("schoolData: ", schoolData)

        if (schoolData && schoolData.bodyParts && schoolData.bodyParts.includes(bodyPart)) {
            bannedSchools.push(schoolname);
        }
    }
    return bannedSchools;
}

function drawBars(svg, stackedData, color, y, x, mouseover, mousemove, mouseleave, clickformoreinfo) {
    svg.selectAll("rect")
        .data(stackedData)
        .join("g")
        .attr("fill", d => color(d.key))
        .attr("class", d => "myRect " + d.key)
        .selectAll("rect")
        .data(d => d)
        .join("rect")
        .attr("y", d => y(d.data.bodyPart))
        .attr("x", d => x(d[0]))
        .attr("width", d => x(d[1]) - x(d[0]))
        .attr("height", y.bandwidth())
        .attr("stroke", "grey")
        .on("mouseover", mouseover)
        .on("mousemove", mousemove)
        .on("mouseleave", mouseleave)
        .on("click", clickformoreinfo);
}

// this method 
/**
 * create the stackable bar graph.
 * @param {*} data formated data for bar grap represantation
 * @param {*} allUniqueStates
 */
function createAstackedGraph(data, allUniqueStates, uniqueBodyParts) {
    allUniqueStates.sort()
    var margin = { top: 80, right: 10, bottom: 50, left: 50 },
        width = 700 - margin.left - margin.right,
        height = 550 - margin.top - margin.bottom;
    var svg = d3.select("#barPlotArea")
        .append("svg")
        .attr("width", width + margin.left + margin.right)
        .attr("height", height + margin.top + margin.bottom)
        .append("g")
        .attr("transform",
            "translate(" + margin.left + "," + margin.top + ")");
    // set the title
    svg
        .append("text")
        .attr("class", "chart-title")
        .attr("x", -(margin.left) * 0.8 + 15)
        .attr("y", -(margin.top) / 2)
        .attr("text-anchor", "start")
        .text("Banned Body Parts in US High School Dress Codes")
        .style("font-size", 20)
        .style("font-weight", "bold")

    // set the Y axis labels
    svg
        .append("text")
        .attr("class", "chart-label")
        .attr("x", width / 2)
        .attr("y", height + margin.bottom / 1.4)
        .attr("text-anchor", "middle")
        .text("Number of schools")

    let maxCount = d3.max(data, function (d) {
        return (Math.max(d.AK, d.AL, d.AR, d.AR, d.AZ, d.CA, d.CO, d.CT, d.DE, d.FL, d.GA, d.IA, d.KS,
            d.MA, d.MD, d.MI, d.MN, d.MO, d.MS, d.MT, d.NC, d.ND, d.NH, d.NM, d.NV, d.NY, d.OH, d.OK, d.OR, d.PA, d.TX, d.UT, d.VA, d.VT, d.WA, d.WI, d.WY))
    });

    var color = d3.scaleOrdinal()
        .domain(allUniqueStates)
        .range(d3.schemeSet2);

    // set y scale
    var y = d3.scaleBand()
        .domain(uniqueBodyParts)
        .range([0, height])
        .padding(.1)
    svg.append('g')
        .call(d3.axisLeft(y).tickSize(0).tickPadding(8));

    // set x scale
    var x = d3.scaleLinear()
        .domain([0, 350])
        .range([0, width])
    svg.append('g')
        .attr("transform", "translate(0," + height + ")")
        .call(d3.axisBottom(x).tickSizeOuter(0))

    // stack data
    var stackedData = d3.stack()
        .keys(allUniqueStates)
        (data)
    console.log("stackedData: ", stackedData)

    // create tooltip
    const tooltip = d3.select("#barPlotArea")
        .append("div")
        .attr("id", "chart")
        .attr("class", "tooltip");
    var smalltooltip = d3.select("body")
        .append("div")
        .attr("class", "tooltipsmall")
        .style("opacity", 0.5)
        .style("font-size", "12px")
        .style("fill", "white")
        .style("background-color", "rgba(255, 255, 255, 0.6)")
        .style("padding", "5px")
        .style("border-radius", "5px")
        .style("position", "absolute");

    // What happens when user hover a bar
    var mousemove = function (event, d) {
        var subgroupName = d3.select(this.parentNode).datum().key;
        d3.selectAll(".myRect").style("opacity", 0.3)
        d3.selectAll("." + subgroupName).style("opacity", 5)
        var countOfSchools = d[1] - d[0];
        var bodyPart = d.data.bodyPart;
        let listOfSchoolsInState = getSchoolsInAGivenState(subgroupName, bodyPart)
        displayToolips(subgroupName, countOfSchools, event, listOfSchoolsInState, bodyPart)
        // displaying the tooltip at the top of each stack.
        var mouseX = event.pageX - 20
        var mouseY = event.pageY - 30
        // Update the content and position of the smalltooltip element
        smalltooltip.text(`${subgroupName} - ${countOfSchools} schools`)
            .style("top", mouseY + "px")
            .style("left", mouseX + "px")
            .style("opacity", 0.9); // Show the smalltooltip

    }

    const mouseover = function (event, d) {
        // mouseover 's job is to show
        tooltip.style("opacity", 1);
        smalltooltip.style("opacity", 1);
    }

    var displayToolips = function (subgroupName, countOfSchools, event, listOfSchoolsInState, bodyPart) {
        var tooltipText = `Body Part: ${bodyPart} <br>State: ${subgroupName} <br>Banned in ${countOfSchools} schools  <br><br>`;
        for (const schoolName of listOfSchoolsInState) {
            tooltipText += schoolName + "<br>";
        }

        tooltip
            .style("opacity", .8)
            .html(tooltipText)
            .style("top", (event.pageY) + 10 + "px")
            .style("left", (event.pageX) + "px")
            .style("overflow-y", "auto");
    }

    var stateIsClicked = false; // Flag to track clicked state
    var mouseleave = function (event, d) {
        if (!stateIsClicked) {
            d3.selectAll(".myRect").style("opacity", 0.8)
        }
        tooltip.style("opacity", 0.5)  // this tooltip should stay visible in case user wants to scroll through schools
        smalltooltip.style("opacity", 0).text(""); //Hiding and clear the smalltooltip element
    }

    // this handle will allow user to highlight the state they clicked and be able to scoll through schools.
    //user should click the state again to revert to the initial display of the bar chart.
    var clickformoreinfo = function (event, d) {
        console.log("clicked", d.data.bodyPart)

        var subgroupName = d3.select(this.parentNode).datum().key;
        if (!stateIsClicked) {
            // Highlight the subgroup if it's not already clicked
            d3.selectAll(".myRect").style("opacity", 0.3);
            d3.selectAll("." + subgroupName).style("opacity", 1);
            stateIsClicked = true; // Update the flag

        } else {
            // Unhighlight the subgroup if it's already clicked
            d3.selectAll("." + subgroupName).style("opacity", 0.8);
            stateIsClicked = false; // Update the flag
        }
        // Rest of your click handling logic
        var countOfSchools = d[1] - d[0];
        var bodyPart = d.data.bodyPart;
        let listOfSchoolsInState = getSchoolsInAGivenState(subgroupName, bodyPart);
        displayToolips(subgroupName, countOfSchools, event, listOfSchoolsInState, bodyPart);
    }

    drawBars(svg, stackedData, color, y, x, mouseover, mousemove, mouseleave, clickformoreinfo)

    // append the text for the small tooltip
    svg.append("text")
        .attr("id", "smalltoll")
        .attr("class", "tooltipsmall")
        .style("opacity", 2); // Set initial opacity to 0

}
/**
 * creates data in proper format for bar chart
 * @param {*} jsonData
 * @returns an array of object
 */
function createDataForBarChart(jsonData) {
    const bodyPartAndSchoolCountByState = {};
    jsonData.forEach((school) => {
        if (school.bodyParts !== null) {
            school.bodyParts.forEach((bodyPart) => {
                if (bodyPart != null) {
                    if (bodyPartAndSchoolCountByState.hasOwnProperty(bodyPart)) {
                        // Incrementing the count for the specific body part and state combination.
                        bodyPartAndSchoolCountByState[bodyPart][school.state] = (bodyPartAndSchoolCountByState[bodyPart][school.state] || 0) + 1;
                    } else {
                        bodyPartAndSchoolCountByState[bodyPart] = { [school.state]: 1 };
                    }
                }
            });
        }
    });
    const allStates = Array.from(new Set(jsonData.map((school) => school.state)));
    // updating the count  of schools in states. Some states might not have banned that body part.therefore school count should be zero 
    Object.values(bodyPartAndSchoolCountByState).forEach((stateCounts) => {
        allStates.forEach((state) => {
            if (!stateCounts[state]) {
                stateCounts[state] = 0;
            }
        });
    });
    //  create an array of objects
    const result = Object.entries(bodyPartAndSchoolCountByState).map(([bodyPart, states]) => ({
        bodyPart,
        ...states,
    }));

    return result;
}
function dictSchoolsInState(jsonData) {
    let dict_schools_in_State = {};
    for (const item of jsonData) {
        if (item.schoolName != null && item.state != null) {
            // check if schoolInStateDic contains the state already 
            if (dict_schools_in_State.hasOwnProperty(item.state)) {
                // update the list of schools in that state
                dict_schools_in_State[item.state].push(item.schoolName);

            } else {
                // If the state is not already present, create a new list and add the current schoolName to it
                let listOfSchools = []
                listOfSchools.push(item.schoolName)
                dict_schools_in_State[item.state] = listOfSchools
            }
        }
    }
    return dict_schools_in_State;

}
