<svelte:head>
	<title>Airport Terminal</title>
</svelte:head>

<script>
	import { TextField, Select } from 'svelte-materialify/src';
	import CardForm from '../components/CardForm.svelte';

	const aircrafts = [
    { name: 'AirCraft1', value: 'AirCraft1' },
    { name: 'AirCraft2', value: 'AirCraft2' },
  ];

	let valid = false;
	let formData = {
		number: "1983",
		gateAssignment: {
			terminal:"A1",
	    number: "12"
		},
		equipment: {
		  carrier: "EMB",
	    tailNumber: "2011"
		},
		schedule: {
			scheduledDeparture : 1613066697198,
	    scheduledArrival: 1613066908474,
	    departureStatus: {
				actual: 1613066918474
    	}
		}
	}

</script>

<CardForm title="Airport Terminal" prevLink="fleet-crew" nextLink="air-traffic-control" isNextDisabled={false}>
	<TextField outlined>Gate Agent Name</TextField>
	<Select outlined items={aircrafts}>Flight</Select>

	<TextField outlined>Equipment - Terminal</TextField>
	<TextField outlined>Equipment - Number</TextField>

	<TextField outlined>Gate Assignment - Terminal</TextField>
	<TextField outlined>Gate Assignment - Number</TextField>

	<TextField outlined>Scheduled Departure</TextField>
	<TextField outlined>Scheduled Arrival</TextField>

	<TextField outlined>Departure Status - Delayed By</TextField>
	<TextField outlined>Departure Status - Actual</TextField>
</CardForm>

