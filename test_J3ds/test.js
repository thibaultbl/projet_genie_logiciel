var sales = [
  { product: 'Hoodie',  count: 12 },
  { product: 'Jacket',  count: 7 },
  { product: 'Snuggie', count: 6 },
];

var pie = d3.layout.pie()
  .value(function(d) { return d.count })

var slices = pie(sales);
// the result looks roughly like this:
[
  {
    data: sales[0],
    endAngle: 3.0159289474462017,
    startAngle: 0,
    value: 12
  },
  {
    data: sales[1],
    startAngle: 3.0159289474462017,
    endAngle: 4.775220833456486,
    value: 7
  },
  {
    data: sales[2],
    startAngle: 4.775220833456486,
    endAngle: 6.283185307179587,
    value: 6
  }
]
