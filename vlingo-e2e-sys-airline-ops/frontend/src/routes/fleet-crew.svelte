<svelte:head>
	<title>Fleet Crew</title>
</svelte:head>

<script>
	import { Select, Button, Dialog, Row } from 'svelte-materialify/src';
	import CardForm from '../components/CardForm.svelte';
	import { Api } from "../api";
	import { flights } from "../stores/flights.js";
	import { inventories } from "../stores/inventory.js";
	import { fleetcrews } from "../stores/fleetcrew.js";
	import { required } from "../util/validators.js";

	let isDialogActive = false;
	let valid = false;

	let selectedAircraft = "";
	let selectedFlight = "";

	let formData = {
		aircraftId: "05e5b41c-1fc7-4946-b04a-fb7a43d9d119",
		carrier: "EMB",
		flightNumber: "1983",
		tailNumber: "2011"
	}

	const submit= async () => {
		const res = await Api.post("/fleetcrew/aircrafts/", formData);
		console.log(res);
		if (res) {
			$fleetcrews = [...$fleetcrews, res]
			toggleDialog()
		}
	}

	const toggleDialog = () => {
		isDialogActive = !isDialogActive;
	}

	$: aircrafts = $inventories.map((inv => {
		return {
			name: inv.manufacturerSpecification.manufacturer,
			value: inv.id,
			...inv
		};
	}));

	$: flightsForSelect = $flights.map((f => {
		return {
			name: f.id,
			value: f.id,
			...f
		};
	}));

	$: {
		if (selectedAircraft) {
			const { id, carrier, registration } = aircrafts.find(airc => airc.id === selectedAircraft);
			formData.aircraftId = id;
			formData.carrier = carrier.name;
			formData.tailNumber = registration.tailNumber;
		}
	}

	$: {
		if (selectedFlight) {
			const f = flightsForSelect.find(flight => flight.id === selectedFlight);
			formData.flightNumber = f.id;
		}
	}

	$: valid = !!selectedAircraft && !!selectedFlight;
</script>

<CardForm title="Fleet Crew" prevLink="flight-planning" nextLink="airport-terminal" isNextDisabled={$fleetcrews.length < 1}>
	<table class="mb-6">
		<thead>
			<tr>
				<th>#</th>
				<th>Departure Airport Code</th>
				<th>Arrival Airport Code</th>
				<th>Cancelled</th>
				<th>#</th>
			</tr>
		</thead>
		<tbody>
			{#each $fleetcrews as crew, ind (ind)}
				<tr>
					<td>{ind + 1}</td>
					<td>{crew}</td>
					<td>{crew}</td>
					<td>{crew}</td>
					<td></td>
				</tr>
			{/each}
		</tbody>
	</table>
	<Button on:click={toggleDialog}>New Fleet Crew</Button>
	<Dialog persistent class="pa-8" bind:active={isDialogActive}>
		<form on:submit|preventDefault={submit} style="min-height: 500px;">
			<Select rules={[required]} outlined items={aircrafts} bind:value={selectedAircraft}>Aircraft</Select>
			<Select rules={[required]} outlined items={flightsForSelect} bind:value={selectedFlight}>Flight</Select>
			<Row class="ml-0 mr-0">
				<div style="flex:1; text-align: left;">
					<Button class="{valid ? 'success-color' : ''}" type="submit" disabled={!valid}>Create</Button>
				</div>
				<Button class="ml-3" type="reset">Reset</Button>
				<Button class="error-color  ml-3" on:click={toggleDialog}>Cancel</Button>
			</Row>
		</form>
	</Dialog>
</CardForm>
