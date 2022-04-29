export function PlayerStatistics() {
    let ranking = {
        playersRanking: [
            {
                playerId: "270f59f3-1faa-41c1-9e91-1d2df166b2ce",
                position: 1,
                classification: "3,000 003 1005",
                points: 3,
                matches: 1,
                victories: 1,
                draws: 0,
                defeats: 0,
                goalsInFavor: 8,
                goalsAgainst: 3,
                balance: 5
            },
            {
                playerId: "665015d9-b55f-4c1d-8bea-9feb40cd0f95",
                position: 1,
                classification: "3,000 003 1005",
                points: 3,
                matches: 1,
                victories: 1,
                draws: 0,
                defeats: 0,
                goalsInFavor: 8,
                goalsAgainst: 3,
                balance: 5
            },
            {
                playerId: "9cb5d61d-01b8-499a-a63f-fee3e9f27990",
                position: 3,
                classification: "0,000 000 0995",
                points: 0,
                matches: 1,
                victories: 0,
                draws: 0,
                defeats: 1,
                goalsInFavor: 3,
                goalsAgainst: 8,
                balance: -5
            },
            {
                playerId: "16a75158-ab37-4d86-9717-8ca323543131",
                position: 3,
                classification: "0,000 000 0995",
                points: 0,
                matches: 1,
                victories: 0,
                draws: 0,
                defeats: 1,
                goalsInFavor: 3,
                goalsAgainst: 8,
                balance: -5
            },
        ]
    }
    return <main>
        <h1>Player Statistics</h1>

        <table className="table">
            <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col">Player</th>
                <th scope="col">Classification</th>
                <th scope="col">Points</th>
                <th scope="col">Matches</th>
                <th scope="col">Victories</th>
                <th scope="col">Draws</th>
                <th scope="col">Defeats</th>
                <th scope="col">Goals in Favor</th>
                <th scope="col">Goals Against</th>
                <th scope="col">Balance</th>
            </tr>
            </thead>
            <tbody>
            {ranking.playersRanking.map((playerRanking, index) =>
                <tr key={index}>
                    <th scope="row">{playerRanking.position}</th>
                    <td>{playerRanking.playerId}</td>
                    <td>{playerRanking.classification}</td>
                    <td>{playerRanking.points}</td>
                    <td>{playerRanking.matches}</td>
                    <td>{playerRanking.victories}</td>
                    <td>{playerRanking.draws}</td>
                    <td>{playerRanking.defeats}</td>
                    <td>{playerRanking.goalsInFavor}</td>
                    <td>{playerRanking.goalsAgainst}</td>
                    <td>{playerRanking.balance}</td>
                </tr>
            )}
            </tbody>
        </table>
    </main>;
}