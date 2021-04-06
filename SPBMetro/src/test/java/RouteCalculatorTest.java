import core.Line;
import core.Station;
import junit.framework.TestCase;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RouteCalculatorTest extends TestCase
{
    List<Station> lineDirect;
    List<Station> lineWithOneChange;
    List<Station> lineWithTwoChange;
    StationIndex stationIndex;
    RouteCalculator calculator;

    Station komsomolskaya;
    Station dostoevskaya;
    Station mendeleevskaya;
    Station chehovskaya;

    @Override
    public void setUp() throws Exception
    {
        stationIndex = new StationIndex();

        Line line1 = new Line(1, "Серая");
        Line line2 = new Line(2, "Салатовая");
        Line line3 = new Line(3, "Красная");

        mendeleevskaya = new Station("Менделеевская", line1);
        Station zvetnoiBulvar = new Station("Цветной бульвар", line1);
        chehovskaya = new Station("Чеховская", line1);
        dostoevskaya = new Station("Достоевская", line2);
        Station trubnaya = new Station("Трубная", line2);
        Station sretenskiiBulvar= new Station("Сретенский бульвар", line2);
        komsomolskaya= new Station("Комсомольская", line3);
        Station krasnieVorota= new Station("Красные ворота", line3);
        Station chistiePrudi = new Station("Чистые пруды", line3);

        Stream.of(line1, line2, line3).forEach(stationIndex::addLine);
        Stream
                .of(mendeleevskaya, zvetnoiBulvar, chehovskaya, dostoevskaya, trubnaya, sretenskiiBulvar, komsomolskaya, krasnieVorota, chistiePrudi)
                .peek(s -> s.getLine().addStation(s)).forEach(stationIndex :: addStation);
        stationIndex.addConnection(Stream.of(zvetnoiBulvar, trubnaya).collect(Collectors.toList()));
        stationIndex.addConnection(Stream.of(sretenskiiBulvar, chistiePrudi).collect(Collectors.toList()));
        stationIndex.getConnectedStations(zvetnoiBulvar);
        stationIndex.getConnectedStations(sretenskiiBulvar);

        calculator = new RouteCalculator(stationIndex);

        lineDirect = Stream.of(mendeleevskaya, zvetnoiBulvar, chehovskaya)
                .collect(Collectors.toList());
        lineWithOneChange = Stream.of(mendeleevskaya, zvetnoiBulvar, trubnaya, dostoevskaya)
                .collect(Collectors.toList());
        lineWithTwoChange= Stream.of(mendeleevskaya, zvetnoiBulvar, trubnaya, sretenskiiBulvar,chistiePrudi, krasnieVorota, komsomolskaya)
                .collect(Collectors.toList());

    }

    public void testCalculateDuration()
    {
        double actual = RouteCalculator.calculateDuration(lineWithTwoChange);
        double expected = 17;
        assertEquals(expected, actual);
    }

    public void testGetShortestRoute() {

        List<Station> actualLineDirect = calculator.getShortestRoute(mendeleevskaya, chehovskaya);
        List<Station> actualLineWithOneChange = calculator.getShortestRoute(mendeleevskaya, dostoevskaya);
        List<Station> actualLineWithTwoChange = calculator.getShortestRoute(mendeleevskaya, komsomolskaya);

        List<Station> expectedLineDirect = lineDirect;
        List<Station> expectedLineWithOneChange = lineWithOneChange;
        List<Station> expectedLineWithTwoChange = lineWithTwoChange;

        assertEquals(actualLineDirect, expectedLineDirect);
        assertEquals(actualLineWithOneChange, expectedLineWithOneChange);
        assertEquals(actualLineWithTwoChange, expectedLineWithTwoChange);
    }

    @Override
    public void tearDown() throws Exception
    {
        super.tearDown();
    }
}

